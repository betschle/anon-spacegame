package com.n.a.ui;

import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;

/**
 * Creates standardized UI components for navigation purposes,
 * such as breadcrumbs, sliders, search field, pagination
 * or menus dealing with navigational steps (Active Components).
 * Components created here must be simple and contain
 * little logic or dependencies.
 */
public class NavigationalComponentFactory { // TODO make protected

    private Skin skin;

    /**
     * Class intended for the sole purpose to group together
     * info for a scroll panel.
     */
    public static class ScrollPaneInfo {
        /** The content to be scrolled; this is where to add more elements to! */
        public Table scrollContent;
        public ScrollPane scrollPane;
        /** A container that contains the scroll pane. This component needs to be
         * added to the parent component. */
        public Table container;
    }


    public NavigationalComponentFactory( Skin skin) {
        this.skin = skin;
    }

    /**
     * Creates a horizontally scrolling content panel
     * @param scrollPaneStyleName
     * @return
     */
    public ScrollPaneInfo getScrollContentPane(String scrollPaneStyleName, int scrollHeight) {
        ScrollPaneInfo scrollPaneInfo = new ScrollPaneInfo();
        scrollPaneInfo.scrollContent = new Table(skin);
        scrollPaneInfo.scrollContent.align(Align.top);

        scrollPaneInfo.scrollPane = new ScrollPane(scrollPaneInfo.scrollContent, skin, scrollPaneStyleName);
        scrollPaneInfo.scrollPane.setFadeScrollBars(false);

        scrollPaneInfo.container = new Table(skin);
        scrollPaneInfo.container.add(scrollPaneInfo.scrollPane).height(scrollHeight).top();

        return scrollPaneInfo;
    }

    /**
     * Creates a horizontally scrolling content panel using an existing component as container.
     * @param scrollPaneStyleName
     * @return
     */
    public ScrollPaneInfo getScrollContentPane(Table content, String scrollPaneStyleName, int scrollHeight) {
        ScrollPaneInfo scrollPaneInfo = new ScrollPaneInfo();
        scrollPaneInfo.scrollContent = new Table(skin);
        scrollPaneInfo.scrollContent.align(Align.top);

        scrollPaneInfo.scrollPane = new ScrollPane(scrollPaneInfo.scrollContent, skin, scrollPaneStyleName);

        content.add(scrollPaneInfo.scrollPane).height(scrollHeight).top();
        scrollPaneInfo.container = content;

        return scrollPaneInfo;
    }

}
