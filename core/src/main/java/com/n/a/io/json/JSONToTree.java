package com.n.a.io.json;

import com.n.a.util.Probability;
import com.n.a.util.Tree;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class JSONToTree
        extends AbstractJSONConverter
        implements JSONConverter<Tree<Probability>, Map> {

    protected JSONToTree( RepositoryLoader loader ) {
        super(loader);
    }

    private Tree.Node<Probability> convertNodeRecursively(Tree.Node<Probability> root, Map map) {

        // TODO this code is repeated in JSONToPlanetArchetypeSettings
        // its not worth it to add another JSONConverter for this as dependency, so consider implementing
        // this with a common util method instead for simplicity
        Map probabilityData = (HashMap) map.get("object");
        float weight = ((Number) probabilityData.get("weight")).floatValue();
        Object traitID = (String) probabilityData.get("type");
        Probability<String> probability = new Probability(traitID, weight);
        root.setObject( probability );
        //

        List children = (List) map.get("children");
        for( Object child : children ) {
            root.addChild( convertNodeRecursively( new Tree.Node<Probability>(null), (Map) child ));
        }
        return root;
    }

    @Override
    public Tree<Probability> convert(Map input) {
        Tree<Probability> tree = new Tree<>();
        convertNodeRecursively(tree.getRoot(), input);
        return tree;
    }
}
