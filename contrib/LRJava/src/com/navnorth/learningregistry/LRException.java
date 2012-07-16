/**
 * Licensed under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.navnorth.learningregistry;

/**
 * Exceptions thrown by the Learning Registery Exporter
 *
 * @version 0.1
 * @since 2011-11-17
 * @author Todd Brown / Navigation North
 *      <br>
 *      Copyright © 2011 Navigation North Learning Solutions LLC
 *      <br>
 *      Licensed under the Apache License, Version 2.0 (the "License"); See LICENSE
 *      and README.md files distributed with this work for additional information
 *      regarding copyright ownership.
 */
public class LRException extends Exception
{
    // Error codes
    public static final int UNKNOWN = 0;
    public static final int NO_DATA = 1;
    public static final int NO_LOCATOR = 2;
    public static final int INVALID_JSON = 3;
    public static final int NOT_CONFIGURED = 4;
    public static final int BENCODE_FAILED = 5;
    public static final int SIGNING_FAILED = 6;
    public static final int NO_KEY = 7;
    public static final int NO_KEY_STREAM = 8;
    public static final int NULL_FIELD = 9;
    public static final int BATCH_ZERO = 10;
    public static final int NO_DOCUMENTS = 11;
    public static final int NO_RESPONSE = 12;
    public static final int INVALID_RESPONSE = 13;
    public static final int JSON_FAILED = 14;
    public static final int JSON_IMPORT_FAILED = 15;
    public static final int INVALID_JSON_OBJECT = 16;
    public static final int IMPORT_FAILED = 17;
	public static final int INPUT_STREAM_FAILED = 18;
	public static final int SIGNATURE_NOT_FOUND = 19;
	public static final int SIGNATURE_INVALID = 20;
	public static final int MESSAGE_INVALID = 21;
	public static final int INVALID_PUBLIC_KEY = 22;
    
    private Integer code;
    
    /**
     * Create an unknown exception
     */
    public LRException()
    {
        super();
        this.code = 0;
    }
    
    /**
     * Create a specific exception
     *
     * @param code The error code of the exception to be created
     */
    public LRException(Integer code)
    {
        super();
        this.code = code;
    }

    /**
     * Create an exception that also records the underlying cause of this exception
     *
     * @param code The error code of the exception to be created
     * @param cause The underlying cause that triggered this exception
     */
    public LRException(Integer code, Throwable cause)
    {
        super(cause);
        this.code = code;
    }
    
    /**
     * Get the integer value of the exception
     *
     * @return Code of the exception
     */
    public Integer getCode()
    {
        return code;
    }
    
    /**
     * Get the message attached to the exception
     *
     * @return Text of the message of the exception
     */
    public String getMessage()
    {
        if (code == NO_DATA)
            return "A document cannot be added without resource data.";
        else if (code == NO_LOCATOR)
            return "A document cannot be added without a resource locator.";
        else if (code == INVALID_JSON)
            return "The provided JSON could not be compiled. Check that the JSON provided is valid.";
        else if (code == NOT_CONFIGURED)
            return "The exporter must be configured before this action can be taken.";
        else if (code == BENCODE_FAILED)
            return "The document could not be bencoded. The resource data may be an invalid structure.";
        else if (code == SIGNING_FAILED)
            return "The document could not be digitally signed. Check that the private key data is correct.";
        else if (code == NO_KEY)
            return "No private key could be found in the provided private key data.";
        else if (code == NO_KEY_STREAM)
            return "The private key data could not be accessed. Check that a valid local file or valid private key dump is provided.";
        else if (code == NULL_FIELD)
            return "A required field was not provided with valid input.";
        else if (code == BATCH_ZERO)
            return "Batch size must be larger than zero.";
        else if (code == NO_DOCUMENTS)
            return "At least one document must be included in order to send data.";
        else if (code == NO_RESPONSE)
            return "The Learning Registry node did not return a response to the request.";
        else if (code == INVALID_RESPONSE)
            return "The response from the Learning Registry node did not match the form of an expected response.";
        else if (code == JSON_FAILED)
            return "The batch of documents could not be converted to a JSON string. Check that no invalid characters were included in the data of any document.";
        else if (code == JSON_IMPORT_FAILED)
            return "The batch of documents could not be converted to a JSON string. Check that the node did not return an error or invalid JSON.";
        else if (code == IMPORT_FAILED)
            return "Capturing data from the node failed. Please check that the node is running and returning results properly.";
        else if (code == INPUT_STREAM_FAILED)
			return "The provided inputs could not be converted into input streams.";
		else if (code == SIGNATURE_NOT_FOUND)
			return "A signature matching the provided key could not be found.";
		else if (code == SIGNATURE_INVALID)
			return "The signature stream does not contain a valid signature.";
		else if (code == MESSAGE_INVALID)
			return "The message stream could not be parsed.";
		else if (code == INVALID_PUBLIC_KEY)
			return "The public key stream does not contain a valid public key.";
		else
            return "An unknown error has ocurred.";
    }
}