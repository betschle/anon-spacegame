package com.n.a;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.ModelLoader;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g3d.*;
import com.badlogic.gdx.graphics.g3d.attributes.ColorAttribute;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.decals.*;
import com.badlogic.gdx.graphics.g3d.environment.DirectionalLight;
import com.badlogic.gdx.graphics.g3d.loader.ObjLoader;
import com.badlogic.gdx.graphics.g3d.utils.CameraInputController;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.math.Quaternion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;

@Deprecated
public class XYZ3DGame extends ApplicationAdapter {

    public Environment environment;
    public PerspectiveCamera cam;

    public Model model;
    public Model modelReference;

    public List<ModelInstance> models = new ArrayList<ModelInstance>();
    public ModelBatch modelBatch;

    public CameraInputController camController;
    public AssetManager assets;

    TextureAtlas atlas;
    Decal decal;
    DecalBatch decalBatch;

    boolean loading = true;

    @Override
    public void create () {
        cam = new PerspectiveCamera(80, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        cam.near = 1;
        cam.far = 400;
        cam.position.set(0, 0, 5);
        cam.lookAt(0,0,0);

        camController = new CameraInputController(cam);
        Gdx.input.setInputProcessor(camController);

        assets = new AssetManager();
        modelBatch = new ModelBatch();
        decalBatch = new DecalBatch(1000, new CameraGroupStrategy(cam));

        ModelBuilder modelBuilder = new ModelBuilder();
        modelReference = modelBuilder.createSphere(5f, 5f, 5f, 8, 20,
                new Material(),
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates);
        ModelInstance reference = new ModelInstance(modelReference, 0, 0, 0);
        models.add(reference);

        environment = new Environment();
        environment.set(new ColorAttribute(ColorAttribute.AmbientLight, 0.4f, 0.4f, 0.4f, 1f));
        environment.add(new DirectionalLight().set(0.8f, 0.8f, 0.8f, -1f, -0.8f, -0.2f));

        // https://xoppa.github.io/blog/loading-models-using-libgdx/
        assets.load("uvmesh.g3db", Model.class);
        assets.load("gfx/ship/ship.png", Texture.class);
        assets.load("gfx/image_atlas.atlas", TextureAtlas.class);

    }

    private void doneLoading() {
        loading = false;
        assets.finishLoading();
        atlas = assets.get("gfx/image_atlas.atlas", TextureAtlas.class);
        model = assets.get("uvmesh.g3db", Model.class);
        TextureAtlas.AtlasRegion region = atlas.findRegion("ship/ship");

        decal = Decal.newDecal(region);
        decal.setBlending(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        decal.setPosition(0, 10, 10);
        decal.setScale(0.5f, 0.5f);
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
    }

    @Override
    public void render () {

        if (loading && assets.update()) {
            doneLoading();
        }

        Gdx.gl.glViewport(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.1f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);
        // Gdx.gl.glEnable(GL20.GL_DEPTH_TEST);
        cam.update();
        camController.update();

        if( !loading ) {
            modelBatch.begin(cam);
            for( ModelInstance instance : models) {
                modelBatch.render(instance, environment);
            }
            modelBatch.end();

            Gdx.gl20.glDepthMask(false);
            decalBatch.add(decal);
            decalBatch.flush();
            Gdx.gl20.glDepthMask(true);
        }
    }

    @Override
    public void pause() {
        super.pause();
    }

    @Override
    public void resume() {
        super.resume();
    }

    @Override
    public void dispose () {
        modelBatch.dispose();
        model.dispose();
    }
}
