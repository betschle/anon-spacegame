package com.n.a.ui.commons.widgets;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.n.a.ui.NavigationalComponentFactory;
import com.n.a.ui.commons.XYZComponent;
import com.n.a.ui.commons.ButtonClickListener;

import java.util.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * Defines a list of buttons that control what is displayed in
 * the main panel.
 */
public class TextButtonTabs<T extends Button>
        extends Table
        implements XYZComponent {

    private static Logger logger = Logger.getLogger(TextButtonTabs.class.getCanonicalName());;

    private List<T> textButtonList = new ArrayList<>();
    private TextButtonTabModel tabTextModel = new TextButtonTabModel();
    private ButtonSettings buttonSettings = new ButtonSettings();
    private ContentTabSettings contentTabSettings = new ContentTabSettings();
    private Table content;

    private NavigationalComponentFactory.ScrollPaneInfo textButtonScrollPane;
    private NavigationalComponentFactory.ScrollPaneInfo textScrollPane;

    private TextArea textArea;
    private ChangeListener tabChangeListener;
    private ButtonClickListener buttonClickListener;

    /**
     * The internal datamodel of the tabs.
     */
    public static class TextButtonTabModel {
        private Map<String, String> entries = new LinkedHashMap<>();

        public String getEntry(String categoryName) {
            return entries.get(categoryName);
        }

        public void addEntry( String categoryName, String longText) {
            this.entries.put(categoryName, longText);
        }

        public void clear() {
            this.entries.clear();
        }
    }

    /**
     * Specifies the size and style of a gdx Button and its subclasses.
     */
    public static class ButtonSettings {
        /**The Button class to use*/
        public Class<? extends Button.ButtonStyle> styleClass = TextButton.TextButtonStyle.class;
        /**The style name to use. */
        public String styleName = "default";
        /**The min button width. */
        public int minButtonWidth = 150;
        /**The min button height. */
        public int minButtonHeight = 20;
    }

    public static class ContentTabSettings {
        /** The min width of the scrollable text panel */
        public int minContentWidth = 300;
        /** The min height of the scrollable text panel */
        public int minContentHeight = 300;
        /** The min height of the non-scrollable table that contains the scrollable text */
        public int minScrollContainerHeight = 200;
        /** The min height of the non-scrollable table that contains the scrollable text */
        public int minScrollContainerWidth = 200;
        public int align = Align.center;
    }

    public TextButtonTabs(Skin skin) {
        super(skin);
        this.content = new Table(skin);

        // set up text area
        this.textArea = new TextArea("", skin);
        this.textArea.setDisabled(true);

        this.tabChangeListener = new ChangeListener() {
            @Override
            public void changed(ChangeListener.ChangeEvent event, Actor actor) {
                if (actor instanceof Button) {
                    Button button = (Button) actor;
                    // User Object = String, category name
                    String categoryName = (String) button.getUserObject();
                    selectContent(categoryName);
                    updateButtonCheckedStates(button);
                }
                event.stop();
            }
        };
    }

    public List<T> getTextButtonList() {
        return textButtonList;
    }

    public ButtonSettings getButtonSettings() {
        return buttonSettings;
    }

    public void setButtonSettings(ButtonSettings buttonSettings) {
        this.buttonSettings = buttonSettings;
    }

    public ContentTabSettings getContentTabSettings() {
        return contentTabSettings;
    }

    public void setContentTabSettings(ContentTabSettings contentTabSettings) {
        this.contentTabSettings = contentTabSettings;
    }

    /**
     * Updates the checked state for all buttons
     * @param clickedButton the button that was clicked, it will not be unchecked.
     */
    private void updateButtonCheckedStates(Button clickedButton) {
        for( T otherButtons : this.textButtonList) {
            otherButtons.setChecked(Objects.equals(otherButtons, clickedButton));
        }
    }

    private void selectContent(String categoryName) {
        String displayText = this.tabTextModel.entries.get(categoryName);
        this.textArea.setText(displayText != null ? displayText : "");
    }

    @Override
    public void construct() {
        this.content.clear();
        this.clear();

        this.updateFromModel();
        this.content.add(this.textButtonScrollPane.container).padRight(10).fill();
        this.content.add(this.textScrollPane.container)
                .minSize(this.contentTabSettings.minScrollContainerWidth, this.contentTabSettings.minScrollContainerHeight)
                .fill();
        this.add(this.content).expand().top();
    }

    @Override
    public void updateFromModel() {
        for (T button : this.textButtonList) {
            button.removeListener(this.buttonClickListener);
        }
        this.textButtonList.clear();
        this.textButtonScrollPane.scrollContent.clear();

        if( this.tabTextModel != null ) {

            for (String categoryName : this.tabTextModel.entries.keySet()) {
                Button button = getButton(categoryName, this.buttonSettings);
                button.setProgrammaticChangeEvents(false);
                button.addListener(this.tabChangeListener);
                // A simple Button does not always have text, so set the userObject instead
                button.setUserObject( categoryName );
                if (this.buttonClickListener != null) {
                    button.addListener(this.buttonClickListener);
                }
                this.textButtonList.add((T) button);
                this.textButtonScrollPane.scrollContent.add(button)
                        .minSize(this.buttonSettings.minButtonWidth,
                                 this.buttonSettings.minButtonHeight).fill().row();
            }

        }
        this.textArea.setText(""); // reset display text in any circumstance
    }

    // similar to ListWithToolTips, TODO outsource code?
    private Button getButton( String title, ButtonSettings buttonSettings) {
        Button.ButtonStyle buttonStyle = getSkin().get(buttonSettings.styleName, buttonSettings.styleClass);
        if( buttonStyle instanceof ImageTextButton.ImageTextButtonStyle) {
            return new ImageTextButton(title, (ImageTextButton.ImageTextButtonStyle) buttonStyle);
        } else
        if( buttonStyle instanceof TextButton.TextButtonStyle) {
            TextButton button = new TextButton(title, (TextButton.TextButtonStyle) buttonStyle);
            CharSequence text = button.getText();
            return button;
        } else
        if( buttonStyle instanceof Button.ButtonStyle) {
            return new Button(buttonStyle);
        }
        return null;
    }

    public TextButtonTabModel getTabTextModel() {
        return tabTextModel;
    }

    public void setTabTextModel(TextButtonTabModel tabTextModel) {
        this.tabTextModel = tabTextModel;
        this.updateFromModel();
    }

    public void setTextButtonScrollPane(NavigationalComponentFactory.ScrollPaneInfo textButtonScrollPane) {
        this.textButtonScrollPane = textButtonScrollPane;
        this.textButtonScrollPane.scrollPane.setFadeScrollBars(false);
    }

    public void setTextScrollPane(NavigationalComponentFactory.ScrollPaneInfo textScrollPane) {
        this.textScrollPane = textScrollPane;
        this.textScrollPane.scrollContent.add(this.textArea).pad(10).minSize(
                this.contentTabSettings.minContentWidth,
                this.contentTabSettings.minContentHeight);
        this.textScrollPane.scrollContent.pack();
    }

    public ButtonClickListener getButtonClickListener() {
        return buttonClickListener;
    }

    public void setButtonClickListener(ButtonClickListener buttonClickListener) {
        this.buttonClickListener = buttonClickListener;
    }
}
