package cz.zutrasoft.base;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.feedback.IFeedbackMessageFilter;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

/**
 * Custom FeedbackPanel, which applies it's css style according {@link FeedbackMessage} level.
 * 
 * @author Michal Václavek
 */
public class CustomMessageFP extends FeedbackPanel
{
	private static final long serialVersionUID = -3120930129358139043L;

	public CustomMessageFP(String id)
	{
        super(id);
    }

    public CustomMessageFP(String id, IFeedbackMessageFilter filter)
    {
        super(id, filter);
    }

    @Override
    protected String getCSSClass(FeedbackMessage message)
    {
        String css;
        switch (message.getLevel())
        {
            case FeedbackMessage.SUCCESS:
                css = "feedbackSuccess";
                break;
            case FeedbackMessage.INFO:
                css = "feedbackInfo";
                break;
            case FeedbackMessage.ERROR:
                css = "feedbackError";
                break;
            default:
                css = "alert";
        }

        return css;
    }
    
}
