// Copyright 2002-2011, University of Colorado

package edu.colorado.phet.website.translation.entities;

import java.util.List;

import org.hibernate.Session;

import edu.colorado.phet.website.constants.WebsiteConstants;
import edu.colorado.phet.website.data.LocalizedSimulation;
import edu.colorado.phet.website.data.faq.FAQItem;
import edu.colorado.phet.website.data.faq.FAQList;
import edu.colorado.phet.website.util.hibernate.HibernateTask;
import edu.colorado.phet.website.util.hibernate.HibernateUtils;

/**
 * Contains references to all simulation FAQ strings
 */
public class SimulationFAQsEntitiy extends TranslationEntity {

    public SimulationFAQsEntitiy() {
        HibernateUtils.wrapSession( new HibernateTask() {
            public boolean run( Session session ) {
                // get a sorted list of simulations
                List<LocalizedSimulation> lsims = HibernateUtils.getAllVisibleSimulationsWithLocale( session, WebsiteConstants.ENGLISH );
                HibernateUtils.orderSimulations( lsims, WebsiteConstants.ENGLISH );

                // add their FAQ item strings, in order
                for ( LocalizedSimulation lsim : lsims ) {
                    FAQList faqList = lsim.getSimulation().getFaqList();
                    if ( faqList != null ) {
                        for ( Object o : faqList.getFaqItems() ) {
                            FAQItem item = (FAQItem) o;
                            if ( item.isQuestion() ) {
                                addString( item.getQuestionKey() );
                                addString( item.getAnswerKey() );
                            }
                            else {
                                addString( item.getHeaderKey() );
                            }
                        }
                    }
                }
                return true;
            }
        } );
    }

    public String getDisplayName() {
        return "Simulation FAQs";
    }
}
