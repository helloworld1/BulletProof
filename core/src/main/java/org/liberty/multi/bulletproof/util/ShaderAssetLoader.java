package org.liberty.multi.bulletproof.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.SynchronousAssetLoader;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.utils.Array;

public class ShaderAssetLoader
        extends SynchronousAssetLoader<ShaderProgram, AssetLoaderParameters<ShaderProgram>> {
    private static final String FRAGMENT_SHADER_SUFFIX = "_frag.glsl";
    private static final String VERTEX_SHADER_SUFFIX = "_vert.glsl";

    public ShaderAssetLoader(final FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public ShaderProgram load(final AssetManager assetManager,
            final String fileName, final FileHandle file,
            final AssetLoaderParameters<ShaderProgram> parameter) {
        final ShaderProgram shader = new ShaderProgram(resolve(fileName
                + VERTEX_SHADER_SUFFIX), resolve(fileName
                + FRAGMENT_SHADER_SUFFIX));
        if (shader.isCompiled() == false) {

        Gdx.app.log("ShaderAssetLoader", "Shader compiled failed. Error: " + shader.getLog());
            throw new RuntimeException("Shader compile failed: " + shader.getLog());
        }

        Gdx.app.log("ShaderAssetLoader", "Shader compiled successfully");
        return shader;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public Array<AssetDescriptor> getDependencies(String fileName,
            FileHandle file, AssetLoaderParameters<ShaderProgram> parameter) {
        return null;
    }
}
