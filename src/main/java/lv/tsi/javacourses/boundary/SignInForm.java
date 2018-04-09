package lv.tsi.javacourses.boundary;

import lv.tsi.javacourses.control.UserControl;
import lv.tsi.javacourses.control.Util;
import lv.tsi.javacourses.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.faces.view.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;

/**
 * @author Dimitrijs Fedotovs <a href="http://www.bug.guru">www.bug.guru</a>
 */
@ViewScoped
@Named
public class SignInForm implements Serializable {
    private static final Logger logger = LoggerFactory.getLogger(SignInForm.class);
    @Inject
    private UserControl userControl;
    @Inject
    private CurrentUser currentUser;
    @Inject
    private HttpServletRequest request;

    private String email;
    private String password;

    public String signIn() {
        User user = userControl.findUserByEmail(email, true);
        if (user == null) {
            Util.addError("signInForm:email", "Unknown email");
            return null;
        }
        try {
            request.login(email, password);
            currentUser.setSignedInUser(user);
            return "/sign-in.xhtml?faces-redirect=true";
        } catch (ServletException e) {
            logger.error("Sign in error", e);
            Util.addError("signInForm:password", "Wrong password");
        }
        return null;
    }

    public String signOut() {
        try {
            request.logout();
            currentUser.setSignedInUser(null);
            return "/index.xhtml?faces-redirect=true";
        } catch (ServletException e) {
            logger.error("Sign out error", e);
        }
        return null;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
