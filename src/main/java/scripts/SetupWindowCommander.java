package scripts;

import api.Controller;
import api.SetupWindow;
import api.UIMessageSender;
import utils.Message;
import utils.CancelableMessage;

import java.util.List;

public class SetupWindowCommander {
    static Controller controller;
    private final String title;
    SetupWindow setupWindow;
    UIMessageSender messageSender;

    /**
     * Jython needs static injection, Spring won't work for that purpose
     * @param controller controller to be injected
     */
    public static void injectMainController(Controller controller) {
        SetupWindowCommander.controller = controller;
    }

    /**
     * Create the setupWindow
     * @param title title of the setupWindow
     */
    public SetupWindowCommander(String title) {
        this.messageSender = controller.createUIMessageSender();
        this.title = title;

        messageSender.synchroneousSend(
                new Message() {
                    @Override
                    public void execute(Message message) {
                        setupWindow = controller.createSetupWindowGUIInstance(title);
                    }
                }
        );
    }

    public void close() {
        messageSender.synchroneousSend(
                new Message() {
                    @Override
                    public void execute(Message message) {
                        setupWindow.close();
                    }
                }
        );
    }

    public void message(String textToShow) throws InterruptedException, CancelException {
        messageSender.synchroneousSendAndGetResult(
                new CancelableMessage() {
                    @Override
                    public void execute(CancelableMessage message) {
                        setupWindow.message(message, textToShow);
                    }
                }
        );
    }

    public String textbox(String textToShow) throws InterruptedException, CancelException {
        return this.textbox(textToShow, "");
    }

    public String textbox(String textToShow, String defaultValue) throws InterruptedException, CancelException {
        return (String) messageSender.synchroneousSendAndGetResult(
                new CancelableMessage<String>() {
                    @Override
                    public void execute(CancelableMessage message) {
                        setupWindow.textbox(message, textToShow, defaultValue);
                    }

                }
        );
    }

    public String menu(String textToShow, List<String> menuItems) throws InterruptedException, CancelException {
        return this.menu(textToShow, menuItems, "");
    }

    public String menu(String textToShow, List<String> menuItems, String defaultValue) throws CancelException, InterruptedException {
        return (String) messageSender.synchroneousSendAndGetResult(
                new CancelableMessage<String>() {
                    @Override
                    public void execute(CancelableMessage message) {
                        setupWindow.menu(message, textToShow, menuItems);
                    }

                }
        );
    }
}
