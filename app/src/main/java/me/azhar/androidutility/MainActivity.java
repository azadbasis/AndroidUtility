package me.azhar.androidutility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import me.azhar.androidutility.R;
import me.azhar.androidutility.VideoActivity;
import me.azhar.androidutility.symmetric.AESEncyption;
import me.azhar.androidutility.symmetric.Encrypter;
import me.azhar.androidutility.symmetric.SymmetricEncryptionUtils;
import se.simbio.encryption.Encryption;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "Encryption";

    private TextView mTextView;
    private final static String ALGO_RANDOM_NUM_GENERATOR = "SHA1PRNG";
    private final static String ALGO_SECRET_KEY_GENERATOR = "AES";
    private final static int IV_LENGTH = 16; // Default length with Default 128

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTextView = (TextView) findViewById(R.id.log_textView);
        mTextView.setMovementMethod(new ScrollingMovementMethod());

        File inFile = new File("/storage/emulated/0/Movies/black.MKV");
        File outFile = new File("/storage/emulated/0/Movies/black_enc.MKV");
        File outFile_dec = new File("/storage/emulated/0/Movies/black_dec.MKV");
        ///storage/emulated/0/Movies/black.MKV

        try {
            SecretKey key = KeyGenerator.getInstance(ALGO_SECRET_KEY_GENERATOR).generateKey();

            byte[] keyData = key.getEncoded();
            SecretKey key2 = new SecretKeySpec(keyData, 0, keyData.length, ALGO_SECRET_KEY_GENERATOR); //if you want to store key bytes to db so its just how to //recreate back key from bytes array

            byte[] iv = new byte[IV_LENGTH];
            SecureRandom.getInstance(ALGO_RANDOM_NUM_GENERATOR).nextBytes(iv); // If
            // storing
            // separately
            AlgorithmParameterSpec paramSpec = new IvParameterSpec(iv);

            Encrypter.encrypt(key, paramSpec, new FileInputStream(inFile), new FileOutputStream(outFile));
            log("Encrypt Video");
            Encrypter.decrypt(key2, paramSpec, new FileInputStream(outFile), new FileOutputStream(outFile_dec));
            log("decrypt video");
        } catch (Exception e) {
            e.printStackTrace();
        }


        // the Normal Usage
        findViewById(R.id.usage_normal).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log("---- Normal Usage ---------------------------------------------------");
                // it is how to get the Encryption instance. You should use your own key your own salt and your own byte array
                Encryption encryption = Encryption.getDefault("SomeKey", "SomeSalt", new byte[16]);

                String secretText = "This is a text to be cryptoGraph, it can be any string that you want";

                // the method encryptOrNull will cryptoGraph your text and if some error occurs will return null
                // if you want handle the errors you can call the cryptoGraph method directly
                String encrypted = encryption.encryptOrNull(secretText);

                // just printing to see the text and the encrypted string
                log("This is our secret text: " + secretText);
                log("And this is our encrypted text: " + encrypted);

                // now you can send the encrypted text by network or save in disk securely or do wherever
                // that you want, but remember cryptoGraph is not all, we need decrypt too, so lets go do it
                String decrypted = encryption.decryptOrNull(encrypted);

                // the decrypted text should be equals the encrypted
                log("And finally this is our decrypted text: " + decrypted);
            }
        });

        // a Customized Usage
        findViewById(R.id.usage_customized).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log("---- Customized Usage -----------------------------------------------");
                // if you want to change Encryption behavior, maybe to reduce the Iteration Count to get a
                // better performance or also change the Algorithm to a customizable one. You can do this
                // things using your own Encryption.Builder, you can get the default e change few things
                Encryption encryption = null;
                try {
                    encryption = Encryption.Builder.getDefaultBuilder("MyKey", "MySalt", new byte[16])
                            .setIterationCount(1) // use 1 instead the default of 65536
                            .build();
                } catch (NoSuchAlgorithmException e) {
                    log("Something wrong: " + e);
                }

                // we also can generate an entire new Builder
                try {
                    encryption = new Encryption.Builder()
                            .setKeyLength(128)
                            .setKeyAlgorithm("AES")
                            .setCharsetName("UTF8")
                            .setIterationCount(65536)
                            .setKey("mor€Z€cr€tKYss")
                            .setDigestAlgorithm("SHA1")
                            .setSalt("A beautiful salt")
                            .setBase64Mode(Base64.DEFAULT)
                            .setAlgorithm("AES/CBC/PKCS5Padding")
                            .setSecureRandomAlgorithm("SHA1PRNG")
                            .setSecretKeyType("PBKDF2WithHmacSHA1")
                            .setIv(new byte[]{29, 88, -79, -101, -108, -38, -126, 90, 52, 101, -35, 114, 12, -48, -66, -30})
                            .build();
                } catch (NoSuchAlgorithmException e) {
                    log("Something wrong: " + e);
                }

                // now we can use our encryption like we have done in normal usage
                log("Our encryption instance, can't be null: " + encryption);
            }
        });

        // an Async Usage
        findViewById(R.id.usage_async).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                log("---- Async Usage ----------------------------------------------------");
                // the encryption algorithm can take some time and if you cannot lock the thread and wait
                // maybe use an async approach is a good idea, so you can do this like below:
                Encryption encryption = Encryption.getDefault("SomeKey", "SomeSalt", new byte[16]);

                // this method will create a thread and works there, the callback is called when the job is done
                encryption.encryptAsync("This is the text to be cryptoGraph", new Encryption.Callback() {
                    @Override
                    public void onSuccess(String encrypted) {
                        // if no errors occurs you will get your encrypted text here
                        log("My encrypted text: " + encrypted);
                    }

                    @Override
                    public void onError(Exception e) {
                        // if an error occurs you will get the exception here
                        log("Oh no! an error has occurred: " + e);
                    }
                });

                // if really the job is in background, maybe the print will be show before
                log("A print from original thread");

                // you can do the same thing to decrypt with decryptAsync
            }
        });
    }

    private void log(final String message) {
        Log.d(TAG, message);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTextView.append(message + "\n");
            }
        });
    }


    public void cryptoGraph(View view) throws Exception {
        String plainText = "This is the text we are going to hide in plain sight";
        String encrypt = AESEncyption.encrypt(plainText);
        String decrypt = AESEncyption.decrypt(encrypt);


        //SYMMETRICENCRYPTION
        SecretKey key = SymmetricEncryptionUtils.createAESKey();
        //  System.out.println(DatatypeConverter.printHexBinary(key.getEncoded()));
        byte[] initializationVector = SymmetricEncryptionUtils.createInitializationVector();
        byte[] cipherText = SymmetricEncryptionUtils.performAESEncyption(plainText, key, initializationVector);
        //System.out.println(DatatypeConverter.printHexBinary(cipherText));
        String encrypted = Base64.encodeToString(cipherText, Base64.DEFAULT);
        String decrypted = SymmetricEncryptionUtils.performAESDecryption(cipherText, key, initializationVector);
        log("PLAIN TEXT: " + plainText);
        log("encrypt TEXT: " + encrypt);
        log("decrypt TEXT: " + decrypt);
        log("Encrypted TEXT: " + encrypted);
        log("DECRYPTED TEXT: " + decrypted);
    }

    public void cryptoGraphVideo(View view) throws Exception {
        // FileInputStream fis = new FileInputStream(new File("D:/Shashank/inputVideo.avi"));
        FileInputStream fis = new FileInputStream(new File("D:/KIDS/VIDEO/black.MKV"));
        //    File outfile = new File("D:/Shashank/encVideo.avi");
        File outfile = new File("D:/KIDS/VIDEO/encVideo.MKV");
        int read;
        if (!outfile.exists())
            outfile.createNewFile();
//        File decfile = new File("D:/Shashank/decVideo.avi");
        File decfile = new File("D:/KIDS/VIDEO/decVideo.MKV");
        if (!decfile.exists())
            decfile.createNewFile();
        FileOutputStream fos = new FileOutputStream(outfile);
        FileInputStream encfis = new FileInputStream(outfile);
        FileOutputStream decfos = new FileOutputStream(decfile);
        Cipher encipher = Cipher.getInstance("AES");
        Cipher decipher = Cipher.getInstance("AES");
        KeyGenerator kgen = KeyGenerator.getInstance("AES");
        //byte key[] = {0x00,0x32,0x22,0x11,0x00,0x00,0x00,0x00,0x00,0x23,0x00,0x00,0x00,0x00,0x00,0x00,0x00};
        SecretKey skey = kgen.generateKey();
        encipher.init(Cipher.ENCRYPT_MODE, skey);
        CipherInputStream cis = new CipherInputStream(fis, encipher);
        decipher.init(Cipher.DECRYPT_MODE, skey);
        CipherOutputStream cos = new CipherOutputStream(decfos, decipher);
        while ((read = cis.read()) != -1) {
            fos.write((char) read);
            fos.flush();
        }
        fos.close();
        while ((read = encfis.read()) != -1) {
            cos.write(read);
            cos.flush();
        }
        cos.close();
    }

    public void playVideo(View view) {
        startActivity(new Intent(this,VideoActivity.class));
    }
}
