package michaelarn0ld.mastery;

import michaelarn0ld.mastery.ui.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class App {
    public static void main(String[] args) {
        ApplicationContext context = new ClassPathXmlApplicationContext("app-config.xml");
        Controller controller = context.getBean(Controller.class);
        controller.run();
    }
}
