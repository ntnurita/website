package edu.colorado.phet.website.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;
import java.security.SignatureException;
import java.util.Iterator;

import org.bouncycastle.bcpg.ArmoredOutputStream;
import org.bouncycastle.bcpg.BCPGOutputStream;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.PGPException;
import org.bouncycastle.openpgp.PGPPrivateKey;
import org.bouncycastle.openpgp.PGPSecretKey;
import org.bouncycastle.openpgp.PGPSecretKeyRing;
import org.bouncycastle.openpgp.PGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.PGPSignature;
import org.bouncycastle.openpgp.PGPSignatureGenerator;
import org.bouncycastle.openpgp.PGPSignatureSubpacketGenerator;
import org.bouncycastle.openpgp.PGPUtil;
import org.bouncycastle.openpgp.operator.jcajce.JcaPGPContentSignerBuilder;
import org.bouncycastle.openpgp.operator.jcajce.JcePBESecretKeyDecryptorBuilder;

/**
 * Heavily modified from BouncyCastle's example ClearSignedFileProcessor
 */
public class PGPSigner {
    private static int readInputLine( ByteArrayOutputStream bOut, InputStream fIn ) throws IOException {
        bOut.reset();

        int lookAhead = -1;
        int ch;

        while ( ( ch = fIn.read() ) >= 0 ) {
            bOut.write( ch );
            if ( ch == '\r' || ch == '\n' ) {
                lookAhead = readPassedEOL( bOut, ch, fIn );
                break;
            }
        }

        return lookAhead;
    }

    private static int readInputLine( ByteArrayOutputStream bOut, int lookAhead, InputStream fIn ) throws IOException {
        bOut.reset();

        int ch = lookAhead;

        do {
            bOut.write( ch );
            if ( ch == '\r' || ch == '\n' ) {
                lookAhead = readPassedEOL( bOut, ch, fIn );
                break;
            }
        }
        while ( ( ch = fIn.read() ) >= 0 );

        if ( ch < 0 ) {
            lookAhead = -1;
        }

        return lookAhead;
    }

    private static int readPassedEOL( ByteArrayOutputStream bOut, int lastCh, InputStream fIn ) throws IOException {
        int lookAhead = fIn.read();

        if ( lastCh == '\r' && lookAhead == '\n' ) {
            bOut.write( lookAhead );
            lookAhead = fIn.read();
        }

        return lookAhead;
    }

    static PGPSecretKey readSecretKey( InputStream input ) throws IOException, PGPException {
        PGPSecretKeyRingCollection pgpSec = new PGPSecretKeyRingCollection(
                PGPUtil.getDecoderStream( input ) );

        //
        // we just loop through the collection till we find a key suitable for encryption, in the real
        // world you would probably want to be a bit smarter about this.
        //

        Iterator keyRingIter = pgpSec.getKeyRings();
        while ( keyRingIter.hasNext() ) {
            PGPSecretKeyRing keyRing = (PGPSecretKeyRing) keyRingIter.next();

            Iterator keyIter = keyRing.getSecretKeys();
            while ( keyIter.hasNext() ) {
                PGPSecretKey key = (PGPSecretKey) keyIter.next();

                if ( key.isSigningKey() ) {
                    return key;
                }
            }
        }

        throw new IllegalArgumentException( "Can't find signing key in key ring." );
    }

    /*
     * create a clear text signed file.
     */
    public static void signFile( File fileToSign, File keyFile, File outputFile, String passphrase )
            throws IOException, NoSuchAlgorithmException, NoSuchProviderException, PGPException, SignatureException {

        char[] pass = passphrase.toCharArray();
        InputStream keyIn = PGPUtil.getDecoderStream( new FileInputStream( keyFile ) );
        FileOutputStream out = new FileOutputStream( outputFile );

        int digest = PGPUtil.SHA256;

        PGPSecretKey pgpSecKey = readSecretKey( keyIn );
        PGPPrivateKey pgpPrivKey = pgpSecKey.extractPrivateKey( new JcePBESecretKeyDecryptorBuilder().setProvider( "SC" ).build( pass ) );
        PGPSignatureGenerator sGen = new PGPSignatureGenerator( new JcaPGPContentSignerBuilder( pgpSecKey.getPublicKey().getAlgorithm(), digest ).setProvider( "SC" ) );
        PGPSignatureSubpacketGenerator spGen = new PGPSignatureSubpacketGenerator();

        sGen.init( PGPSignature.CANONICAL_TEXT_DOCUMENT, pgpPrivKey );

        Iterator it = pgpSecKey.getPublicKey().getUserIDs();
        if ( it.hasNext() ) {
            spGen.setSignerUserID( false, (String) it.next() );
            sGen.setHashedSubpackets( spGen.generate() );
        }

        InputStream fIn = new BufferedInputStream( new FileInputStream( fileToSign ) );
            ArmoredOutputStream aOut = new ArmoredOutputStream( out );

        aOut.beginClearText( digest );

        //
        // note the last \n/\r/\r\n in the file is ignored
        //
        ByteArrayOutputStream lineOut = new ByteArrayOutputStream();
        int lookAhead = readInputLine( lineOut, fIn );

        processLine( aOut, sGen, lineOut.toByteArray() );

        if ( lookAhead != -1 ) {
            do {
                lookAhead = readInputLine( lineOut, lookAhead, fIn );

                sGen.update( (byte) '\r' );
                sGen.update( (byte) '\n' );

                processLine( aOut, sGen, lineOut.toByteArray() );
            }
            while ( lookAhead != -1 );
        }

        fIn.close();

        aOut.endClearText();

        BCPGOutputStream bOut = new BCPGOutputStream( aOut );

        sGen.generate().encode( bOut );

        aOut.close();
    }

    private static void processLine( OutputStream aOut, PGPSignatureGenerator sGen, byte[] line )
            throws SignatureException, IOException {
        // note: trailing white space needs to be removed from the end of
        // each line for signature calculation RFC 4880 Section 7.1
        int length = getLengthWithoutWhiteSpace( line );
        if ( length > 0 ) {
            sGen.update( line, 0, length );
        }

        aOut.write( line, 0, line.length );
    }

    private static boolean isLineEnding( byte b ) {
        return b == '\r' || b == '\n';
    }

    private static int getLengthWithoutWhiteSpace( byte[] line ) {
        int end = line.length - 1;

        while ( end >= 0 && isWhiteSpace( line[end] ) ) {
            end--;
        }

        return end + 1;
    }

    private static boolean isWhiteSpace( byte b ) {
        return isLineEnding( b ) || b == '\t' || b == ' ';
    }

}

