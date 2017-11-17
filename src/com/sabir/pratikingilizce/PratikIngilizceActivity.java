package com.sabir.pratikingilizce;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Locale;

import org.anddev.andengine.engine.Engine;
import org.anddev.andengine.engine.camera.Camera;
import org.anddev.andengine.engine.handler.IUpdateHandler;
import org.anddev.andengine.engine.handler.timer.ITimerCallback;
import org.anddev.andengine.engine.handler.timer.TimerHandler;
import org.anddev.andengine.engine.options.EngineOptions;
import org.anddev.andengine.engine.options.EngineOptions.ScreenOrientation;
import org.anddev.andengine.engine.options.resolutionpolicy.FillResolutionPolicy;
import org.anddev.andengine.entity.scene.Scene;
import org.anddev.andengine.entity.sprite.Sprite;
import org.anddev.andengine.entity.text.ChangeableText;
import org.anddev.andengine.entity.text.Text;
import org.anddev.andengine.entity.util.FPSCounter;
import org.anddev.andengine.entity.util.FPSLogger;
import org.anddev.andengine.input.touch.TouchEvent;
import org.anddev.andengine.opengl.font.Font;
import org.anddev.andengine.opengl.texture.BuildableTexture;
import org.anddev.andengine.opengl.texture.Texture;
import org.anddev.andengine.opengl.texture.TextureOptions;
import org.anddev.andengine.opengl.texture.region.TextureRegion;
import org.anddev.andengine.opengl.texture.region.TextureRegionFactory;
import org.anddev.andengine.ui.activity.BaseGameActivity;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Typeface;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

import com.sabir.pratikingilizce.R;

public class PratikIngilizceActivity extends BaseGameActivity {
	private static final int CAMERA_WIDTH = 800;
	private static final int CAMERA_HEIGHT = 480;
	private Camera camera;
	private Engine engine;
	Texture textureResim1, textureResim2, textureArkaplan, textureProperties,
			textureIleri,textureGeri,textureAyarlar3,textureKaydet,textureCalismaVar;//,textureCalismaVar
	TextureRegion textRegResim1, textRegResim2, textRegArkaplan,
			textRegProperties, textRegIleri,textRegGeri,textRegAyarlar3,textRegKaydet;
	TextureRegion textureRegCalismaVar;
	Sprite spriteResim1, spriteResim2, spriteArkaplan, spriteProperties,spriteCalismaVar,
			spriteIleri,spriteGeri,spriteAyarlarIleri,spriteAyarlarIleri2,spriteAyarlarIleri3,spriteAyarlarGeri,spriteAyarlarKaydet;
	Scene sahneOyun, sahneAnaMenu, sahnePauseMenu,sahneAyarlar,sahneCalisma;
	Font font,font1, mFont;

	BuildableTexture bTex,cText,ayarlarText,seceneklerText;
	ChangeableText cTexSkor1, cTexSkor2,ayarlarSkor,seceneklerSkor,seceneklerDeger;
	private boolean hareket = false;
	static JSONObject json = null;
	static String jsonString = "";
	StringBuilder sb;
	private int jSonLastData;
	private String[] secenekler={"Kelimeler","Cumleler","Dil Bilgisi"};
	private String[] secenekler2={"slayt","manuel"};
	private String[] secenekler3={"Genis Zaman","Gecmis Zaman","Gelecek Zaman","Edatlar"};
	private boolean slayt_Flag=false;
    private final static int final_count = 300;
    private boolean flag_ayarlar3= true;
    private float EndOfTimer= 10f;
    private String[] jSonDizi = new String[10];
    private boolean ilkEkran=false;
	private SQLiteDatabase database;
	private Font yourFont;
    private static final int toplamKelimeSayisi = 1150;
    private static final int toplamCumleSayisi  = 410;
	private ChangeableText englishText;
	private ChangeableText turkishText;
	private String _in="";
	private String _tr="";
	public PratikIngilizceActivity() {
		
		//super();
//		veritabani = new Veritabani(this);
//		//veritabani.getWritableDatabase();
//		veritabani.insert("cumle", "0");
//		Cursor c = veritabani.getAll();
//		System.out.println(" veritabaný kayýt : "+veritabani.getdeger(c) +" - "+veritabani.getkategori(c));
	}
	private void veritabaniAc(){
		Veritabani veritabani = new Veritabani(this);
		database = veritabani.getReadableDatabase();
	}
	private void verileriYaz(int id,int sayac,String kategori,String dil_bilgisi,String slayt){
		Veritabani veritabani = new Veritabani(this);
		database = veritabani.getWritableDatabase();
		veritabani.insert(id,kategori,sayac,dil_bilgisi,slayt);
	}

	private void verileriOku(Context context) {
		
		Veritabani veritabani = new Veritabani(context);
		database = veritabani.getReadableDatabase();
//		veritabani.insert("sayac","10");
	
		Cursor c = veritabani.getAll();
		if(!c.equals(null)){
		while(c.moveToNext()){
			System.out.println("while ýn içinde");
			Long id = c.getLong(c.getColumnIndex("_id"));
			if(id.equals(1)){
				verileriYaz(1,10,"kelimeler",null, "slayt");
			}
			ayarlar_skor_count = Integer.parseInt(veritabani.getdeger(c));
			if("kelimeler".equalsIgnoreCase(veritabani.getkategori(c))){
				ayarlar_count = 0;
			}else if("Cumleler".equalsIgnoreCase(veritabani.getkategori(c))){
				ayarlar_count = 1;
			}else if("Dil Bilgisi".equalsIgnoreCase(veritabani.getkategori(c))){
				ayarlar_count = 2;
			}
			if("slayt".equalsIgnoreCase(c.getString(c.getColumnIndex("slayt")))){
				ayarlar3_count=0;
			}else{
				ayarlar3_count=1;
			}
			System.out.println(" veritabaný kayýt : "+c.getLong(c.getColumnIndex("_id")));
			System.out.println(" veritabaný kayýt : "+" : "+veritabani.getdeger(c) +" : "+veritabani.getkategori(c));
			System.out.println(" veritabaný kayýt : "+" : "+veritabani.getdil_bilgisi(c));
			System.out.println(" veritabaný kayýt : "+" : "+c.getString(c.getColumnIndex("slayt")));
		 }
		}
	}

	public Engine onLoadEngine() {
		// camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		// final EngineOptions engineOptions = new EngineOptions(true,
		// ScreenOrientation.LANDSCAPE, new RatioResolutionPolicy(CAMERA_WIDTH,
		// CAMERA_HEIGHT), camera);
		// engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		// Engine engine = new Engine(engineOptions);

		camera = new Camera(0, 0, CAMERA_WIDTH, CAMERA_HEIGHT);
		final EngineOptions engineOptions = new EngineOptions(true,
				ScreenOrientation.LANDSCAPE, new FillResolutionPolicy(), camera);
		engineOptions.getTouchOptions().setRunOnUpdateThread(true);
		engineOptions.setNeedsSound(true);
		engine = new Engine(engineOptions);

		return engine;
	}
	public void fontManager(){
		this.mFont = new Font(this.cText, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 36, true, Color.BLUE);
		this.font = new Font(this.bTex, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 36, true, Color.RED);

        Font[] fonts = {font,mFont};
		//this.mFont = FontFactory.create(this.getFontManager(), this.getTextureManager(), 256, 256, TextureOptions.BILINEAR, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 48);
		//this.mFont.load();
        mEngine.getFontManager().loadFonts(fonts);
	}
	 @Override
	public void onLoadResources() {
		textureResim1 = new Texture(256, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textureResim2 = new Texture(256, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textureArkaplan = new Texture(1024, 512,
				TextureOptions.DEFAULT);//1024,512
		textureProperties = new Texture(128, 128,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textureIleri = new Texture(64, 64,
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textureGeri = new Texture(64, 64, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textureAyarlar3 = new Texture(64, 64, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		textureCalismaVar = new Texture(1024, 512,
				TextureOptions.DEFAULT);
		textureKaydet = new Texture(256, 64, 
				TextureOptions.BILINEAR_PREMULTIPLYALPHA);

		textRegResim1 = TextureRegionFactory.createFromAsset(textureResim1,
				this, "gfx/baslat.png", 0, 0);
		textRegResim2 = TextureRegionFactory.createFromAsset(textureResim2,
				this, "gfx/cikis.png", 0, 0);
		textRegArkaplan = TextureRegionFactory.createFromAsset(textureArkaplan,
				this, "gfx/background.png", 0, 0);
		textRegProperties = TextureRegionFactory.createFromAsset(
				textureProperties, this, "gfx/ayarlar.png", 0, 0);
		textRegIleri = TextureRegionFactory.createFromAsset(textureIleri, this,
				"gfx/right-button.png", 0, 0);
		textRegGeri = TextureRegionFactory.createFromAsset(textureGeri, this,
				"gfx/left-button.png", 0, 0);
		textRegAyarlar3 = TextureRegionFactory.createFromAsset(textureGeri, this,
				"gfx/left-button.png", 0, 0);
		textureRegCalismaVar = TextureRegionFactory.createFromAsset(textureCalismaVar, this,
				"gfx/yapimasamasi.png", 0, 0);
		textRegKaydet = TextureRegionFactory.createFromAsset(textureKaydet, this,
				"gfx/save-button.png", 0, 0);
		if(bTex == null){
			bTex = new BuildableTexture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);//,TextureOptions.BILINEAR_PREMULTIPLYALPHA);
		}else{
			bTex = null;
			bTex = new BuildableTexture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);//,TextureOptions.REPEATING_BILINEAR_PREMULTIPLYALPHA);
		}
		if(cText == null){
			cText= new BuildableTexture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);//GabrielSerifBold.ttf
		}else{
			cText = null;
			cText= new BuildableTexture(256, 256,TextureOptions.BILINEAR_PREMULTIPLYALPHA);//GabrielSerifBold.ttf
		}
		
//		  FontFactory.setAssetBasePath("font/");
//		    final ITexture fontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);
//
//		    yourFont = FontFactory.createFromAsset(getFontManager(), fontTexture, getAssets(), "font.ttf", 40, true, Color.BLACK);
//		    yourFont.load(); Color.BLUE, 2,Color.rgb(97, 134, 167
		//this.font = FontFactory.createFromAsset(this.bTex, this,"gfx/Din-Bold.otf", 46, true, Color.RED);
		//this.mFont = FontFactory.createFromAsset(this.cText, this,"gfx/GabrielSerifBold.ttf", 48, true, Color.BLUE);
//		this.mFont = new Font(this.cText, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 46, true, Color.BLUE);
//		this.font = new Font(this.bTex, Typeface.create(Typeface.DEFAULT, Typeface.BOLD), 46, true, Color.RED);
//
//        Font[] fonts = {font,mFont};
//        mEngine.getFontManager().loadFonts(fonts);
		mEngine.getTextureManager().loadTextures(textureResim1, textureResim2,
				textureArkaplan, textureProperties, textureIleri,textureGeri, bTex,cText,textureKaydet,textureCalismaVar);//,textureGeri,textureKaydet
		fontManager();
	}

	Scene scene;
	int counts = 0, count = 0,ayarlar_count=0,ayarlar_skor_count=0,ayarlar3_count=0,ayarlar_dilbilgisi_count=0;
	private String ayarlar_flag="kelimeler";
	private int json_count=0;
	private float slayt_time;
//	private ChangeableText englishText;
//	private ChangeableText turkishText;
	private TextView ingText;
	private TextView trText;
	private Text englishtext;
	private Text turkishtext;
	private Text leftText;
	private Text lefttext;
	//private Text countingText;
	//private Font fontDefault32Bold;
    @Override
	public Scene onLoadScene() {

        englishText = new ChangeableText(30, 170, this.mFont , "", 90);
        turkishText = new ChangeableText(30, 240, this.font, "", 90); 
        
        leftText = new Text(30, 170, mFont, "");
        lefttext = new Text(30,240,font,"");
       
//    	Veritabani veritabani = new Veritabani(this);
//    	try {
//			veritabani.dropTable();
//		} catch (Exception e1) {
//			// TODO Auto-generated catch block
//			Log.e("create db fail", e1.toString());
//		}
//    	veritabaniAc();
    	verileriOku(this);
		this.mEngine.registerUpdateHandler(new FPSLogger());
		this.scene = new Scene();
		try {
			getJsonData();
		} catch (IOException e) {
			Log.e("getJsonData", e.getMessage().toString());
			e.printStackTrace();
		}
		anaMenuNesneleriniOlustur();
		slaytWord();
		ayarlar();
		System.out.println("onLoadScene");
		if(ilkEkran)
			first_screen();
        if(flag_ayarlar3){
        	//auto_slayt();
        	first_screen();
        }
       mEngine.getFontManager().reloadFonts();
		return this.scene;
	}
    private void calismaVar(){
    	sahneCalisma = new Scene();
    	spriteCalismaVar  = new Sprite(0, 0, textureRegCalismaVar){
    		@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					System.out.println("click1 dil");
                    mEngine.setScene(sahneAyarlar);
				}
				return true;
			}
		};	
    	
    	
    	//AnimatedSprite nemy = new AnimatedSprite(0,0,(TiledTextureRegion) textureRegCalismaVar);
    	this.sahneCalisma.attachChild(spriteCalismaVar);
    	this.sahneCalisma.registerTouchArea(spriteCalismaVar);
    	this.mEngine.setScene(sahneCalisma);
    	//return scene;
    }
    private Scene first_screen() {
		// TODO Auto-generated method stub
		System.out.println("first_slayt");
		final FPSCounter fpsCounter = new FPSCounter();
      this.mEngine.registerUpdateHandler(fpsCounter);
      float slayt_times = 0.1f;
      final Scene scenes = new Scene();
      
      //scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));

      final ChangeableText elapsedText = new ChangeableText(250, 170, this.mFont , "", 60);
      final ChangeableText fpsText = new ChangeableText(250, 240, this.font, "", 60);
      
    
      //counts = 0;
     
      scenes.registerUpdateHandler(new TimerHandler(slayt_times, true, new ITimerCallback() {
              @Override
              public void onTimePassed(final TimerHandler pTimerHandler) {
            	  elapsedText.setText("......yükleniyor");
            	  //auto_slayt();
              }
      }));
      scenes.attachChild(elapsedText);
      scenes.attachChild(fpsText);
      //ilkEkran=false;
      this.mEngine.setScene(scenes);
     return scenes;
}
   

	
	private Scene auto_slayt(Float time) {
		// TODO Auto-generated method stub
		System.out.println("auto_slayt");
		final FPSCounter fpsCounter = new FPSCounter();
      this.mEngine.registerUpdateHandler(fpsCounter);
      slayt_time = 3f;
      final Scene scene = new Scene();
      this.font.reload();
      //scene.setBackground(new ColorBackground(0.09804f, 0.6274f, 0.8784f));
//      final ChangeableText englishText;
//  	  final ChangeableText turkishText;
//      englishText = new ChangeableText(180, 170, this.mFont , "", 80);
//      turkishText = new ChangeableText(180, 240, this.font, "", 80);
     
      if("cumleler".equals(ayarlar_flag)){
      	englishText.setPosition(30, 170);
      	turkishText.setPosition(30, 240);
      	slayt_time = 6f;
      	
	  }  
//      scene.attachChild(englishText);
//      scene.attachChild(turkishText);
      //counts = 0;
      //System.out.println("ilkEkran :"+ilkEkran);
     
      scene.registerUpdateHandler(new TimerHandler(slayt_time, true, new ITimerCallback() {
             
    	  
			@Override
              public void onTimePassed(final TimerHandler pTimerHandler) {
				
					String[] ing = null;
					try {
						System.out.println("counts :"+counts);
						System.out.println("ayarlar_skor_count :"+ayarlar_skor_count);	
						//bTex = null;cText = null;
						//onLoadResources();
						//mEngine.getTextureManager().loadTextures(cText);
						fontManager();
						mEngine.getFontManager().reloadFonts();
						String word = slaytDoldur(counts);
							ing = word.split("-");
							String in = ing[0].toLowerCase(Locale.US);
							String tr  = ing[1].toLowerCase();
							System.out.println(in);
							System.out.println(tr);
							 set_in(in);
							 set_tr(tr);
							 englishText.setText(get_in());
							 turkishText.setText(get_tr());

					} catch (IOException e) {
						Log.e("onTimePassed deki auto_slayt", e.getMessage().toString());
						e.printStackTrace();
					}
					if(counts<9)
					    counts++;
					else
						counts = 0;
//                      elapsedText.setText("Seconds elapsed: " + PratikIngilizceActivity.this.mEngine.getSecondsElapsedTotal());
//                      fpsText.setText("FPS: " + Math.round(fpsCounter.getFPS()));
            	    //scene.clearChildScene();
					System.gc();
					//mEngine.clearUpdateHandlers();
					}
		

      }));
      scene.attachChild(englishText);
      scene.attachChild(turkishText);
//      scene.attachChild(leftText);
//      scene.attachChild(lefttext);
      this.mEngine.setScene(scene);
     return scene;
}
	

	private void ayarlar() {
		// ayarlar ekraný
	
		this.sahneAyarlar  = new Scene();
//		if(ayarlar_skor_count==10){
//			spriteAyarlarGeri.setVisible(false);
//		}

		ayarlarSkor    = new ChangeableText(360, 150, this.font, "", 40);
		seceneklerSkor     = new ChangeableText(300,210, this.font, "",40);
		seceneklerDeger    = new ChangeableText(340,270, this.font, "",40);
		this.font.reload();
		font.prepareLettes(".:1234567890".toCharArray());
		if("DilBilgisi".equals(ayarlar_flag)){
			ayarlarSkor.setPosition(120, 150);
			System.out.println("ayarlar_flag :"+ayarlar_flag);
		}
		
		spriteAyarlarIleri = new Sprite(540, 130, textRegIleri){
			
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					if(!"DilBilgisi".equals(ayarlar_flag)){
					  if("Kelimeler".equals(secenekler[ayarlar_count])){
						if(ayarlar_skor_count!=toplamKelimeSayisi)
						    ayarlar_skor_count +=10;
						 if(ayarlar_skor_count>10){
							 spriteAyarlarGeri.setVisible(true);
						 }
					   }else if("Cumleler".equals(secenekler[ayarlar_count])){
							if(ayarlar_skor_count!=toplamCumleSayisi)
							    ayarlar_skor_count +=10;
							 if(ayarlar_skor_count>10){
								 spriteAyarlarGeri.setVisible(true);
							 }
						   }
					}else{
						spriteAyarlarGeri.setVisible(false);
						if(ayarlar_dilbilgisi_count <3 ){
							ayarlar_dilbilgisi_count ++;
						}else{
							ayarlar_dilbilgisi_count = 0;
						}
					}
				}
				return true;
			}
			
			
			
		};
		spriteAyarlarGeri  = new Sprite(200, 130, textRegGeri){
			
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					if(ayarlar_skor_count>10){
						ayarlar_skor_count -=10;
					}
				}
				return true;
			}
			
			
			
		};
		spriteAyarlarIleri2 = new Sprite(540, 200, textRegIleri){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					if(ayarlar_count<2){
					    ayarlar_count++;
					    System.out.println("ayarlar_count :"+ayarlar_count);
					}else{
						ayarlar_count = 0;
					}
				}
				return true;
			}
			
			
			
		};
		spriteAyarlarIleri3 = new Sprite(540, 270, textRegIleri){			
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					if(ayarlar3_count<1){
						ayarlar3_count++;
						if(!"Slayt".equals(secenekler2[ayarlar3_count]))
							flag_ayarlar3 = false;
						//System.out.println("flag_ayarlar3 :"+flag_ayarlar3 +" "+ayarlar3_count);
					}else{
						ayarlar3_count = 0;
						if(!"Manuel".equals(secenekler2[ayarlar3_count]))
							flag_ayarlar3 = true;
						//System.out.println("flag_ayarlar3 :"+flag_ayarlar3+" "+ayarlar3_count);
						
					}
				}
				return true;
			}
			
			
		};

		spriteAyarlarKaydet= new Sprite(270,340,textRegKaydet){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY){
				//int sayac,String kategori,String dil_bilgisi,String slayt)
				   verileriYaz(2,ayarlar_skor_count, ayarlar_flag,secenekler3[ayarlar_dilbilgisi_count],secenekler2[ayarlar3_count]);
				   engine.setScene(scene);
				return true;
			}
		};
		//seceneklerSkor.setText(secenekler[0]);
		if(ayarlar_skor_count==0)
			ayarlar_skor_count = 10;
		this.sahneAyarlar.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() {
			  onLoadResources();
			}

			@Override
			public void onUpdate(float pSecondsElapsed) {
				if("Dil Bilgisi".equals(secenekler[ayarlar_count])){
					ayarlar_flag = "DilBilgisi";
					try {
						getJsonData();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					spriteAyarlarGeri.setVisible(false);
				}else{
					ayarlarSkor.setPosition(360, 150);
				}if("Cumleler".equals(secenekler[ayarlar_count])){
					ayarlar_flag = "cumleler";
					if(ayarlar_skor_count>toplamCumleSayisi)
						ayarlar_skor_count=toplamCumleSayisi;
					spriteAyarlarGeri.setVisible(true);
					ilkEkran=true;
					try {
						getJsonData();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}if("Kelimeler".equals(secenekler[ayarlar_count])){
					ayarlar_flag = "kelimeler";
					spriteAyarlarGeri.setVisible(true);
					ilkEkran=true;
					try {
						getJsonData();
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				if("DilBilgisi".equals(ayarlar_flag)){
					if("Genis Zaman".equals(secenekler3[ayarlar_dilbilgisi_count]))
						 ayarlarSkor.setPosition(220, 150);
					if("Gecmis Zaman".equals(secenekler3[ayarlar_dilbilgisi_count]))
						ayarlarSkor.setPosition(210, 150);
					if("Gelecek Zaman".equals(secenekler3[ayarlar_dilbilgisi_count]))
						ayarlarSkor.setPosition(190, 150);
					if("Edatlar".equals(secenekler3[ayarlar_dilbilgisi_count]))
						ayarlarSkor.setPosition(300, 150);
					ayarlarSkor.setText(secenekler3[ayarlar_dilbilgisi_count]);
				}else{
					//String ayarlar_str = Integer.toString(ayarlar_skor_count);
					//String ayarlar_str = ""+ayarlar_skor_count;
					//ayarlarSkor.reset();
					//bTex = null;cText = null;
					onLoadResources();
					
					ayarlarSkor.setText(String.valueOf(ayarlar_skor_count));
				}
				seceneklerSkor.setText(secenekler[ayarlar_count].toLowerCase(new Locale("tr","TR")));
				
				seceneklerDeger.setText(secenekler2[ayarlar3_count].toLowerCase(new Locale("tr","TR")));
				sahneAyarlar.clearChildScene();
				System.gc();
			}
              
		});
		
		
		this.sahneAyarlar.attachChild(ayarlarSkor);
		this.sahneAyarlar.attachChild(spriteAyarlarIleri);
		this.sahneAyarlar.attachChild(spriteAyarlarIleri2);
		this.sahneAyarlar.attachChild(spriteAyarlarIleri3);
		this.sahneAyarlar.attachChild(seceneklerSkor);
		this.sahneAyarlar.attachChild(seceneklerDeger);
		this.sahneAyarlar.attachChild(spriteAyarlarKaydet);
		this.sahneAyarlar.attachChild(spriteAyarlarGeri);
		this.sahneAyarlar.registerTouchArea(spriteAyarlarIleri);
		this.sahneAyarlar.registerTouchArea(spriteAyarlarIleri2);
		this.sahneAyarlar.registerTouchArea(spriteAyarlarIleri3);
		this.sahneAyarlar.registerTouchArea(spriteAyarlarGeri);
		this.sahneAyarlar.registerTouchArea(spriteAyarlarKaydet);
		this.mEngine.setScene(sahneAyarlar);
	}

	private void slaytWord() {
		// TODO Auto-generated method stub
		this.sahneOyun = new Scene();
        
		this.sahneOyun.registerUpdateHandler(new IUpdateHandler() {

			@Override
			public void reset() {
//				try {
//					getJsonData();
//				} catch (IOException e1) {
//					// TODO Auto-generated catch block
//					e1.printStackTrace();
//				}
			}
          
			@Override
			public void onUpdate(float pSecondsElapsed) {
				if (hareket) {
					counts = count;
					String[] ing = null;
					try {
						String word = slaytDoldur(counts);
						//EndOfTimer -= pSecondsElapsed;
						//if(EndOfTimer<=0){
							ing = word.split("-");
							cTexSkor1.setText(ing[0].toLowerCase());
							cTexSkor2.setText(ing[1].toLowerCase());
							
							
						//}if(EndOfTimer==0)
						//	EndOfTimer = 10f;

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					
					// ((ChangeableText)
					// countingText).setText("GAME :"+String.valueOf(counts));
					// ((ChangeableText)
					// countingText).setText("OYUN :"+String.valueOf(counts));
					System.out.println("update count :" + counts);
					// sahneOyun.clearUpdateHandlers();
					sahneOyun.clearChildScene();
				}
			}

		});
		
		// this.sahneOyun.setOnSceneTouchListener( new IOnSceneTouchListener() {
		//
		// @Override
		// public boolean onSceneTouchEvent(Scene pScene, TouchEvent
		// pSceneTouchEvent)
		// {
		// switch(pSceneTouchEvent.getAction())
		// {
		// case TouchEvent.ACTION_MOVE:
		// //if(pSceneTouchEvent.getX() < CAMERA_WIDTH/2 - 64)
		// //{
		// count++;
		// System.out.println("touch :"+count);
		// //}
		// break;
		// }
		// return true;
		// }
		// });
		cTexSkor1 = new ChangeableText(30, 40, this.mFont, "", 60);
		cTexSkor2 = new ChangeableText(30, 160, this.font, "", 60);
		if (count == 0) {
			String[] ing = null;
			try {
				String word = slaytDoldur(count);
				ing = word.split("-");
				cTexSkor1.setText(ing[0].toLowerCase());
				cTexSkor2.setText(ing[1].toLowerCase());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}
		// if(counts == 1){
		// System.out.println("create count :"+counts);
		// cTexSkor1.setText("Games  :"+counts);
		// cTexSkor2.setText("Oyunlar  :"+counts);
		// }
		// countingText = new Text(400f, 240f,fontDefault32Bold, "0");

		spriteIleri = new Sprite(680, 410, textRegIleri) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					hareket = true;
					if(count==9)
						count=0;
					count++;
					if(count==9)
						count=0;
					//System.out.println("count :" + count);
				}
				return true;
			}
		};
		spriteGeri = new Sprite(30,410,textRegGeri){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					hareket = true;
					if(count==0)
						count=9;
					count--;
					if(count==0)
						count=9;
					//System.out.println("count :" + count);
				}
				return true;
			}
		};
		this.sahneOyun.attachChild(cTexSkor1);
		this.sahneOyun.attachChild(cTexSkor2);
		this.sahneOyun.attachChild(spriteIleri);
		this.sahneOyun.attachChild(spriteGeri);
		this.sahneOyun.registerTouchArea(spriteIleri);
		this.sahneOyun.registerTouchArea(spriteGeri);
		this.mEngine.setScene(sahneOyun);
	}

	private void anaMenuNesneleriniOlustur() {
		spriteResim1 = new Sprite(30, 60, textRegResim1) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionUp()) {
					System.out.println("click1");
					if("DilBilgisi".equals(ayarlar_flag)){
						//flag_ayarlar3 = false;
						calismaVar();
					}else
						
                    if(flag_ayarlar3){
                    	//first_screen();
                      fontManager();
                         auto_slayt(0.1f);	
                    }else if(!flag_ayarlar3){
                    	//first_screen();
                    	engine.setScene(sahneOyun);
                    }
				}
				return true;
			}
		};
		spriteResim2 = new Sprite(600, 60, textRegResim2) {
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,
					float pTouchAreaLocalX, float pTouchAreaLocalY) {
				if (pSceneTouchEvent.isActionDown()) {
					// anaMenuCikis.oSprite.setVisible(false);
					// anaMenuCikisHover.oSprite.setVisible(true);
				}
				if (pSceneTouchEvent.isActionUp()) {
					System.exit(0);
				}
				return true;
			}
		};
		spriteArkaplan = new Sprite(0, 0, textRegArkaplan);
		spriteProperties = new Sprite(300, 220, textRegProperties){
			@Override
			public boolean onAreaTouched(TouchEvent pSceneTouchEvent,float pTouchAreaLocalX,float pTouchAreaLocalY){
				if (pSceneTouchEvent.isActionDown()) {
					
				}
				if (pSceneTouchEvent.isActionUp()) {
					engine.setScene(sahneAyarlar);
				}
				return true;
			}
			
		};

		this.scene.attachChild(spriteArkaplan);
		this.scene.attachChild(spriteResim1);
		this.scene.attachChild(spriteResim2);
		this.scene.attachChild(spriteProperties);

		this.scene.registerTouchArea(spriteResim1);
		this.scene.registerTouchArea(spriteResim2);
		this.scene.registerTouchArea(spriteProperties);

	}

	public void onLoadComplete() {

	}

	private String slaytDoldur(int artir)throws IOException {
		
		    //System.out.println("jSonDizi :"+jSonDizi[artir]);
			return jSonDizi[artir];
		

	}
	public void getJsonData()throws IOException {
		//int read_path = R.raw.cumleler;
		System.out.println("ayarlar_flag "+ayarlar_flag);
		System.out.println("ayarlar_skor_count "+ayarlar_skor_count);
		AllRemoveJsonDizi();
		ilkEkran=true;
        InputStream is = this.getResources().openRawResource(R.raw.kelimeler);
        int i = 0,ayarlar_counts=0;
        if(ayarlar_skor_count>0){
        	ayarlar_counts =ayarlar_skor_count -1;
        	  if(ayarlar_counts>=9)
        		  ayarlar_counts = ayarlar_counts-9;
        }
		try {
			if("kelimeler".equals(ayarlar_flag)){
				is = this.getResources().openRawResource(R.raw.kelimeler);
			}
			if("cumleler".equals(ayarlar_flag)){
				is = this.getResources().openRawResource(R.raw.cumleler);
			}
			if("Dil Bilgisi".equals(ayarlar_flag)){
				is = this.getResources().openRawResource(R.raw.cumleler);
			}
			BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-9"));//"iso-8859-9"
			sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
				}
			
		while(i<=9){
			JSONObject req = new JSONObject(sb.toString());
			JSONObject locs = req.getJSONObject("veriler");
			JSONArray recs = locs.getJSONArray(ayarlar_flag);

			JSONObject rec = recs.getJSONObject(ayarlar_counts);
			String ing = rec.getString("ing");
			String tr = rec.getString("tr");

			is.close();
			jsonString = sb.toString();
             ing += "-";
             ing += tr;
             jSonDizi[i] = ing;
             i++;
             System.out.println("ayarlar_counts "+ayarlar_counts);
             ayarlar_counts++;
		  }  
		} catch (JSONException e) {
			Log.e("JSON Parser", "Error parsing data " + e.toString());
		}
	}
	public void AllRemoveJsonDizi(){
		for(int i=0;i<9;i++){
			jSonDizi[i]="";
		}
	}
	// Fiziksel tuþlarýn kullanýmýna olanak veren onKeyDown Metodu
	@Override
	public boolean onKeyDown(final int pKeyCode, final KeyEvent pEvent)
	{
		// Geri Tuþuna basýldýðýnda yapýlacaklar
		if(pKeyCode == KeyEvent.KEYCODE_BACK && pEvent.getAction() == KeyEvent.ACTION_DOWN) 
		{
			
			engine.setScene(scene);
			return true;
		}
		// Menu tuþuna basýldýðýnda yapýlacaklar
		else if(pKeyCode == KeyEvent.KEYCODE_MENU && pEvent.getAction() == KeyEvent.ACTION_DOWN) 
		{		
			
			engine.setScene(scene);
			return true;
		}
		else 
		{
			return super.onKeyDown(pKeyCode, pEvent);
		}
	}
	public String get_in() {
		return _in;
	}
	public void set_in(String _in) {
		this._in = _in;
	}
	public String get_tr() {
		return _tr;
	}
	public void set_tr(String _tr) {
		this._tr = _tr;
	}

}
