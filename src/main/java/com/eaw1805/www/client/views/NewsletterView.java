package com.eaw1805.www.client.views;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.MouseEvent;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RichTextArea;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.eaw1805.data.constants.NewsConstants;
import com.eaw1805.data.dto.common.NationDTO;
import com.eaw1805.www.client.events.loading.LoadEventManager;
import com.eaw1805.www.client.events.loading.NationsLoadedEvent;
import com.eaw1805.www.client.events.loading.NationsLoadedHandler;
import com.eaw1805.www.client.gui.eventHandlers.DelEventHandlerAbstract;
import com.eaw1805.www.client.remote.EmpireRpcService;
import com.eaw1805.www.client.remote.EmpireRpcServiceAsync;
import com.eaw1805.www.client.views.infopanels.ChatOptionInfoPanel;
import com.eaw1805.www.client.widgets.DraggablePanel;
import com.eaw1805.www.client.widgets.ErrorPopup;
import com.eaw1805.www.client.widgets.ImageButton;
import com.eaw1805.www.client.widgets.RichTextToolbar;
import com.eaw1805.www.client.widgets.SelectEAW;
import com.eaw1805.www.client.widgets.StyledCheckBox;
import com.eaw1805.www.shared.stores.DataStore;
import com.eaw1805.www.shared.stores.GameStore;


public class NewsletterView extends DraggablePanel implements NewsConstants {

    private final EmpireRpcServiceAsync eService = GWT.create(EmpireRpcService.class);


    private final ImageButton wrNewsImg;
    private final ImageButton sndPersonalImg;
    private String personalMessage = "";
    private String newsletterMessage = "";
    int mode = 0;//0 for newsletter, 1 for personal message


    private int nationSelected = 0;
    private final HorizontalPanel privateContainer = new HorizontalPanel();
    private final RichTextArea textArea = new RichTextArea();
    private final Label title;
    private final StyledCheckBox anonymous;

    public NewsletterView() {
        this.setStyleName("newletterPanel");
        this.setSize("900px", "632px");

        title = new Label("Newsletter");
        title.setStyleName("whiteText clearFontMedLarge");
        add(title, 12, 10);

        anonymous = new StyledCheckBox("anonymous announcement", false, false);
        anonymous.setSize("320px", "30px");

        final RichTextToolbar toolBar = new RichTextToolbar(textArea);

        wrNewsImg = new ImageButton("http://static.eaw1805.com/images/panels/newsletter/ButWriteNewsOff.png");
        wrNewsImg.setSize("255px", "");
        wrNewsImg.setSelected(true);
        add(this.wrNewsImg, 15, 50);

        (new DelEventHandlerAbstract() {

            public void execute(final MouseEvent event) {
                if (mode == 1) {
                    personalMessage = textArea.getHTML();
                    textArea.setHTML(newsletterMessage);
                }
                mode = 0;
                privateContainer.setVisible(false);
                anonymous.setVisible(true);
                sndPersonalImg.deselect();
                updateTitle();
            }
        }).addToElement(wrNewsImg.getElement()).register();

        sndPersonalImg = new ImageButton("http://static.eaw1805.com/images/panels/newsletter/ButSendMessageOff.png");
        sndPersonalImg.setSize("255px", "");
        add(sndPersonalImg, 275, 50);

        (new DelEventHandlerAbstract() {

            public void execute(final MouseEvent event) {
                if (mode == 0) {
                    newsletterMessage = textArea.getHTML();
                    textArea.setHTML(personalMessage);
                }
                mode = 1;
                privateContainer.setVisible(true);
                anonymous.setVisible(false);
                wrNewsImg.deselect();
                updateTitle();
            }
        }).addToElement(sndPersonalImg.getElement()).register();

        add(anonymous, 570, 53);

        (new DelEventHandlerAbstract() {
            public void execute(MouseEvent event) {
                updateTitle();
            }
        }).addToElement(anonymous.getCheckBox().getElement()).register();

        LoadEventManager.addNationsLoadedHandler(new NationsLoadedHandler() {
            public void onNationsLoaded(final NationsLoadedEvent event) {
                privateContainer.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);

                final SelectEAW selector = new SelectEAW() {
                    @Override
                    public void onChange() {
                        nationSelected = (Integer) this.getValue();
                        updateTitle();
                    }
                };
                selector.setOptionsStyles("optionDefault320x30", "optionHover320x30", "optionSelected320x30");

                for (NationDTO nation : DataStore.getInstance().getNations()) {
                    if (GameStore.getInstance().getNationId() != nation.getNationId()
                            && nation.getNationId() > 0) {
                        selector.addOption(new ChatOptionInfoPanel(false, false, nation), nation.getNationId());
                    }
                }
                selector.setSize(320, 30, 377, 420, 9, 20, 20, 23);
                selector.setDropDownStyleName("dropDown320x420");
                selector.selectOption(1);
                privateContainer.add(selector);
                add(privateContainer, 540, 50);
                privateContainer.setVisible(false);
            }
        });

        final VerticalPanel vp = new VerticalPanel();
        vp.add(toolBar);
        vp.add(textArea);

        textArea.setWidth("100%");
        textArea.setHeight("481px");
        textArea.setStyleName("textAreaNoBorder");
        vp.setWidth("874px");
        add(vp, 9, 82);

        final ImageButton imgX = new ImageButton("http://static.eaw1805.com/images/buttons/ButCancelHover.png");
        imgX.setStyleName("pointer");
        final NewsletterView self = this;

        (new DelEventHandlerAbstract() {
            public void execute(final MouseEvent event) {
                imgX.deselect();
                GameStore.getInstance().getLayoutView().removeWidgetFromPanelEAW(self);
                GameStore.getInstance().getLayoutView().getOptionsMenu().getNewsImg().deselect();
            }
        }).addToElement(imgX.getElement()).register();

        imgX.setSize("31px", "31px");
        add(imgX, 858, 8);

        final ImageButton sendImg = new ImageButton("http://static.eaw1805.com/images/buttons/ButAcceptOff.png");
        add(sendImg, 825, 8);
        sendImg.setSize("31px", "31px");
        (new DelEventHandlerAbstract() {
            public void execute(final MouseEvent event) {
                //init variables
                int type;
                if (mode == 1) {
                    type = NEWS_PRIVATE;
                } else {
                    if (anonymous.isChecked()) {
                        type = NEWS_LETTER_ANONYMOUS;

                    } else {
                        type = NEWS_LETTER;
                    }
                }
                final String message = textArea.getHTML();

                //send message
                eService.saveNewsletter(GameStore.getInstance().getScenarioId(), GameStore.getInstance().getNationId(), GameStore.getInstance().getGameId(), nationSelected, message, type, new AsyncCallback<Integer>() {
                    @Override
                    public void onFailure(Throwable throwable) {
                        new ErrorPopup(ErrorPopup.Level.WARNING, "Message could not be sent! it appears that you are offline. Click here for the login screen to open, login again, and then try to send it again.", false) {
                            public void onAccept() {
                                Window.open("/login", "_blank", "");
                            }
                        };
                    }

                    @Override
                    public void onSuccess(Integer integer) {
                        new ErrorPopup(ErrorPopup.Level.NORMAL, "Message sent", false);
                    }
                });

                //clear fields
                textArea.setHTML("");
                if (mode == 0) {
                    newsletterMessage = "";
                } else {
                    personalMessage = "";
                }


            }
        }).addToElement(sendImg.getElement()).register();
    }

    private void updateTitle() {
        final StringBuilder titleBuilder = new StringBuilder();

        if (mode == 0) {
            titleBuilder.append("Send ");
            if (anonymous.isChecked()) {
                titleBuilder.append("anonymous ");
            }
            titleBuilder.append("announce to Newsletter");

        } else {
            titleBuilder.append("Private message to ").append(DataStore.getInstance().getNationNameByNationId(nationSelected));
        }
        title.setText(titleBuilder.toString());
    }
}
