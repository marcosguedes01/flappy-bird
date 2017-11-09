package com.programacaobrasil.flappybird;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.Random;

public class FlappyBird extends ApplicationAdapter {

	private float variacao;
	private float velocidadeQueda;
	private float posicaoInicialVertical;
    private float posicaoMovimentoCanoHorizontal;
    private float espacoEntreCanos;
    private float deltaTime;
    private float alturaEntreCanosRandomica;
    private boolean marcouPonto = false;

	private float widthScreen;
	private float heightScreen;
    private int estadoJogo = 0; // 0-> jogo não iniciado 1-> jogo iniciado
    private int pontuacao = 0;

	private SpriteBatch batch;

    private Texture fundo;
    private Texture canoBaixo;
    private Texture canoTopo;
	private Texture[] passaros;
    private BitmapFont fonte;
    private BitmapFont mensagem;
    private Circle passaroCirculo;
    private Rectangle canoTopoRect;
    private Rectangle canoBaixoRect;
    private Texture gameOver;
    //private ShapeRenderer shape;

    private Random numeroRandomico;

    private OrthographicCamera camera;
    private Viewport viewport;
    private final float VIRTUAL_WIDTH = 768;
    private final float VIRTUAL_HIGHT = 1024;

	@Override
	public void create () {
		variacao = 0;
		velocidadeQueda = 0;

		widthScreen = VIRTUAL_WIDTH;
		heightScreen = VIRTUAL_HIGHT;
		posicaoInicialVertical = heightScreen / 2;
        posicaoMovimentoCanoHorizontal = widthScreen;
        espacoEntreCanos = 300;

		batch = new SpriteBatch();
        numeroRandomico = new Random();

        initializeComponents();
        configureCamera();

		//Gdx.app.log("create", "Initializado o jogo");
	}

	private void initializeComponents()
    {
        passaroCirculo = new Circle();
        canoBaixoRect = new Rectangle();
        canoTopoRect = new Rectangle();
        //shape = new ShapeRenderer();

        fonte = new BitmapFont();
        fonte.setColor(Color.WHITE);
        fonte.getData().setScale(6);

        mensagem = new BitmapFont();
        mensagem.setColor(Color.WHITE);
        mensagem.getData().setScale(3);

        fundo = new Texture("fundo.png");

        canoBaixo = new Texture("cano_baixo.png");
        canoTopo = new Texture("cano_topo.png");

        passaros = new Texture[3];
        passaros[0] = new Texture("passaro1.png");
        passaros[1] = new Texture("passaro2.png");
        passaros[2] = new Texture("passaro3.png");

        gameOver = new Texture("game_over.png");
    }

    private void configureCamera()
    {
        //Configuração da câmera
        camera = new OrthographicCamera();
        camera.position.set(VIRTUAL_WIDTH/2, VIRTUAL_HIGHT/2, 0);
        viewport = new StretchViewport(VIRTUAL_WIDTH, VIRTUAL_HIGHT, camera);
    }

	@Override
	public void render () {
        camera.update();

        //Limpar frames anteriores
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT | GL20.GL_DEPTH_BUFFER_BIT);

        deltaTime = Gdx.graphics.getDeltaTime();
        variacao += deltaTime * 10;
        if (variacao > 2) variacao = 0;

        if (estadoJogo==0){ // não iniciado
            if (Gdx.input.justTouched()) {
                estadoJogo = 1;
            }
        }
        else {
            // iniciado
            velocidadeQueda++;
            if (posicaoInicialVertical > 0 || velocidadeQueda < 0) {
                posicaoInicialVertical = Math.max(posicaoInicialVertical - velocidadeQueda, 0);
            }

            if (estadoJogo==1)
            {
                posicaoMovimentoCanoHorizontal -= deltaTime * 200;

                if (Gdx.input.justTouched()) {
                    velocidadeQueda = -15;
                }

                // Verifica se o cano saiu da tela
                if (posicaoMovimentoCanoHorizontal < -100) {
                    posicaoMovimentoCanoHorizontal = widthScreen;
                    alturaEntreCanosRandomica = numeroRandomico.nextInt(400) - 200;
                    marcouPonto = false;
                }

                // Verifica pontuação
                if (posicaoMovimentoCanoHorizontal < 120)
                {
                    if (!marcouPonto)
                    {
                        pontuacao++;
                        marcouPonto = true;
                    }
                }
            }
            else
            {
                // game over
                if (Gdx.input.justTouched())
                {
                    estadoJogo = 0;
                    pontuacao=0;
                    velocidadeQueda=0;
                    posicaoInicialVertical = heightScreen/2;
                    posicaoMovimentoCanoHorizontal = widthScreen;
                    marcouPonto = false;
                }
            }
        }

        // Configurar dados de projeção da câmera
        batch.setProjectionMatrix(camera.combined);

		batch.begin();

		batch.draw(fundo, 0,0, widthScreen, heightScreen);
        batch.draw(canoTopo, posicaoMovimentoCanoHorizontal, heightScreen/2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica);
        batch.draw(canoBaixo, posicaoMovimentoCanoHorizontal, heightScreen/2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica);
		batch.draw(passaros[ (int)variacao], 120, posicaoInicialVertical);
        fonte.draw(batch, String.valueOf(pontuacao), widthScreen/2, heightScreen - 50);

        if (estadoJogo==2)
        {
            batch.draw(gameOver, widthScreen/2-gameOver.getWidth()/2, heightScreen/2-gameOver.getHeight()/2);
            mensagem.draw(batch, "Toque para reiniciar!", widthScreen/2-200, heightScreen/2 - gameOver.getHeight()/2 - 10);
        }

		batch.end();
		//Gdx.app.log("render", "deltaTime: " + deltaTime);

        passaroCirculo.set(120 + passaros[0].getWidth()/2, posicaoInicialVertical + passaros[0].getHeight() / 2, passaros[0].getWidth()/2);
        canoBaixoRect.set(posicaoMovimentoCanoHorizontal, heightScreen/2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + alturaEntreCanosRandomica, canoBaixo.getWidth(), canoBaixo.getHeight());
        canoTopoRect.set(posicaoMovimentoCanoHorizontal, heightScreen/2 + espacoEntreCanos / 2 + alturaEntreCanosRandomica, canoTopo.getWidth(), canoTopo.getHeight());

        //Desenhar formas
        /*shape.begin(ShapeRenderer.ShapeType.Filled);
        shape.circle(passaroCirculo.x, passaroCirculo.y, passaroCirculo.radius);
        shape.rect(canoBaixoRect.x, canoBaixoRect.y, canoBaixoRect.getWidth(), canoBaixo.getHeight());
        shape.rect(canoTopoRect.x, canoTopoRect.y, canoTopoRect.getWidth(), canoTopoRect.getHeight());
        shape.setColor(Color.RED);
        shape.end();*/

        //Teste de colisão
        if (
                Intersector.overlaps(passaroCirculo, canoBaixoRect) ||
                        Intersector.overlaps(passaroCirculo, canoTopoRect) ||
                        posicaoInicialVertical<=0 ||
                        posicaoInicialVertical>=heightScreen)
        {
            estadoJogo = 2;
            //Gdx.app.log("Colisão", "Houve Colisão");
        }
	}

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
    }
}