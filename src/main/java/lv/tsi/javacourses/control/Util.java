package lv.tsi.javacourses.control;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;

/**
 * @author Dimitrijs Fedotovs <a href="http://www.bug.guru">www.bug.guru</a>
 */
public class Util {
    public static void addError(String fieldId, String message) {
        FacesContext.getCurrentInstance()
                .addMessage(fieldId,
                        new FacesMessage(FacesMessage.SEVERITY_ERROR, message, null));
    }
}
