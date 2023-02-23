package com.n.a.ui.commons.widgets;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.ArrayList;
import java.util.List;

/**
 * A wizard combines multiple views to
 * obtain user input for different topics or objects.
 * In some way works like a tabbed panel except the
 * contents are flying into the screen from the side.
 */
public class Wizard extends Group {

    List<Table> contents = new ArrayList<>();

}
