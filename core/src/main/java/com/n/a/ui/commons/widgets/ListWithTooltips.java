package com.n.a.ui.commons.widgets;

import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.n.a.ui.commons.XYZComponent;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A list of buttons that displays tooltips
 * with more information about the buttons on hover.
 * More compact than {@link TextButtonTabs}.
 */
public class ListWithTooltips<T extends Button> extends Table implements XYZComponent {

    /** The amount of columns this list creates from the buttons. */
    private float columnCount = 1;
    private ListWithTooltipsModel listModel;
    private TextButtonTabs.ButtonSettings buttonSettings;
    private List<T> buttons = new ArrayList<>();

    public static class ListWithTooltipsModel {
        private Map<String, ToolTipModel> entries = new LinkedHashMap<>();

        public ToolTipModel getEntry(String itemText) {
            return entries.get(itemText);
        }

        public void addEntry( String itemText, String tooltipText) {
            ToolTipModel model = new ToolTipModel(null, itemText, null, tooltipText);
            this.entries.put(itemText, model);
        }

        public void addEntry( String buttonIcon, String buttonText, String toolTipIcon, String toolTipText ) {
            ToolTipModel model = new ToolTipModel(buttonIcon, buttonText, toolTipIcon, toolTipText  );
            this.entries.put(buttonText, model);
        }

        public void clear() {
            this.entries.clear();
        }

        public TextButtonTabs.TextButtonTabModel toTextButtonTabModel() {
            // TODO
            TextButtonTabs.TextButtonTabModel model = null;
            return model;
        }
    }

    // TODO this works but is a bit lazy
    public static class ToolTipModel {
        public String toolTipIcon;
        public String toolTipText;
        public String buttonIcon;
        public String buttonText;

        public ToolTipModel(String buttonIcon, String buttonText, String toolTipIcon, String toolTipText) {
            this.toolTipIcon = toolTipIcon;
            this.toolTipText = toolTipText;
            this.buttonIcon = buttonIcon;
            this.buttonText = buttonText;
        }
    }

    public ListWithTooltips(Skin skin) {
        super(skin);
    }

    public float getColumnCount() {
        return columnCount;
    }

    public void setColumnCount(float columns) {
        this.columnCount = columns;
    }

    public ListWithTooltipsModel getListModel() {
        return listModel;
    }

    public void setListModel(ListWithTooltipsModel listModel) {
        this.listModel = listModel;
        this.updateFromModel();
        this.construct();
    }

    public TextButtonTabs.ButtonSettings getButtonSettings() {
        return buttonSettings;
    }

    public void setButtonSettings(TextButtonTabs.ButtonSettings buttonSettings) {
        this.buttonSettings = buttonSettings;
    }

    @Override
    public void construct(){
        this.clear();
        int counter = 1;
        for( T button : this.buttons) {
            this.add(button).minSize(   this.buttonSettings.minButtonWidth,
                                        this.buttonSettings.minButtonHeight).fill();
            if( counter == this.columnCount ) {
                this.row();
                counter = 1;
            } else {
                counter++;
            }
        }
        this.pack();
    }

    @Override
    public void updateFromModel() {
        // cleanup, this should remove old tooltips from manager
        for( T button : this.buttons) {
            button.clearListeners();
        }
        this.buttons.clear();

        if( this.listModel != null) {
            for (Map.Entry<String, ToolTipModel> entry : this.listModel.entries.entrySet()) {
                ToolTipModel toolTipModel = entry.getValue();

                Button button = getButton(toolTipModel, this.buttonSettings);
                button.setDisabled(true);

                Tooltip<Table> tooltip = getTooltip(toolTipModel);
                button.addListener(tooltip);
                this.buttons.add((T) button);
            }
        }
    }

    private Tooltip<Table> getTooltip(ToolTipModel toolTipModel) {
        Table panel = new Table(this.getSkin());
        panel.setBackground("label_transparent");

        TextArea textArea = new TextArea(toolTipModel.toolTipText, this.getSkin());
        textArea.setDisabled(true);
        panel.add( new Image(this.getSkin(), "icon_info")).size(32, 32).pad((10)).top();
        // approximate line calculation
        int width = 320;
        int avgLetterWidth = 14;
        int avgLetterHeight = 15;
        int lines = ( (toolTipModel.toolTipText.length() * avgLetterWidth) / width ) ;
        //
        panel.add(textArea).minSize(width, lines * avgLetterHeight).pad(10);
        Tooltip<Table> tableTooltip = new Tooltip<>(panel);
        return tableTooltip;
    }

    private Button getButton( ToolTipModel toolTipModel, TextButtonTabs.ButtonSettings buttonSettings) {
        Button.ButtonStyle buttonStyle = getSkin().get(buttonSettings.styleName, buttonSettings.styleClass);
        if( buttonStyle instanceof ImageTextButton.ImageTextButtonStyle) {
            ImageTextButton.ImageTextButtonStyle imageTextButtonStyle = (ImageTextButton.ImageTextButtonStyle) buttonStyle;
            ImageTextButton.ImageTextButtonStyle imageTextButtonStyleCopy = new ImageTextButton.ImageTextButtonStyle();

            // copy most important fields
            imageTextButtonStyleCopy.up = imageTextButtonStyle.up;
            imageTextButtonStyleCopy.down = imageTextButtonStyle.down;
            imageTextButtonStyleCopy.checked = imageTextButtonStyle.checked;
            imageTextButtonStyleCopy.checkedOver = imageTextButtonStyle.checkedOver;
            imageTextButtonStyleCopy.checkedDown = imageTextButtonStyle.checkedDown;
            imageTextButtonStyleCopy.over = imageTextButtonStyle.up;
            imageTextButtonStyleCopy.font = imageTextButtonStyle.font;
            imageTextButtonStyleCopy.fontColor = imageTextButtonStyle.fontColor;
            imageTextButtonStyleCopy.overFontColor = imageTextButtonStyle.overFontColor;

            // use icons from tooltipmodel
            imageTextButtonStyleCopy.imageUp = getSkin().getDrawable( toolTipModel.buttonIcon );
            imageTextButtonStyleCopy.imageDown = getSkin().getDrawable( toolTipModel.buttonIcon );
            imageTextButtonStyleCopy.imageChecked = getSkin().getDrawable( toolTipModel.buttonIcon );

            return new ImageTextButton(toolTipModel.buttonText, imageTextButtonStyleCopy);
        } else
        if( buttonStyle instanceof TextButton.TextButtonStyle) {
            TextButton button = new TextButton(toolTipModel.buttonText, (TextButton.TextButtonStyle) buttonStyle);
            CharSequence text = button.getText();
            return button;
        } else
        if( buttonStyle instanceof Button.ButtonStyle) {
            Button.ButtonStyle buttonStyleCopy = new Button.ButtonStyle();
            buttonStyleCopy.up = getSkin().getDrawable( toolTipModel.buttonIcon );
            buttonStyleCopy.down = getSkin().getDrawable( toolTipModel.buttonIcon );
            buttonStyleCopy.over = getSkin().getDrawable( toolTipModel.buttonIcon );
            buttonStyleCopy.checked = getSkin().getDrawable( toolTipModel.buttonIcon );
            return new Button(buttonStyleCopy);
        }
        return null;
    }
}
