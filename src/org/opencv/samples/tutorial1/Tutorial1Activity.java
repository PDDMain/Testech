package org.opencv.samples.tutorial1;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import org.opencv.android.*;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.w3c.dom.Text;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.*;
import java.util.*;


public class Tutorial1Activity extends Activity implements CvCameraViewListener2 {
    private static final String TAG = "OCVSample::Activity";

    private CameraBridgeViewBase mOpenCvCameraView;
    private boolean mIsJavaCamera = true;

    private Mat mRgba;
    private Mat mGray;
    private Mat lastCameraFrame;

    private Double MID1 = 0.0;
    private Double[][] mid2;
    private Double[][] mid3;

//    private int QUANTITY_X = 7;
//    private int QUANTITY_Y = 4;

    private int MIN_MID2_FOR_TRUE = 88;
    private int MAX_MID2_FOR_FALSE = 88;
    private int MAX_MID3_FOR_TRUE = 88;
    private int MAX_MID3_FOR_FALSE = 88;

    private Student lastStudent = new Student(new boolean[Student.QUANTITY_QUESTIONS][Student.QUANTITY_OPTIONS]);

    private boolean[][] RESULT = lastStudent.studentAnswer;
    private boolean[][] ANSWER = lastStudent.trueAnswer;

    private int SUM_RESULT = 0;
    private int MARK;

    private MenuItem mItemSendResult;
    private MenuItem mItemClean;
    private MenuItem mItemSaveAnswer;
    private MenuItem mItemCheckStudent;

    public double SIDE_OF_SQUARE = 1.0 / 18.0;

    private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch (status) {
                case LoaderCallbackInterface.SUCCESS: {
                    Log.i(TAG, "OpenCV loaded successfully");
                    mOpenCvCameraView.enableView();
                }
                break;
                default: {
                    super.onManagerConnected(status);
                }
                break;
            }
        }
    };

    public Tutorial1Activity() {
        Log.i(TAG, "Instantiated new " + this.getClass());
    }

    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.i(TAG, "called onCreate");
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.tutorial1_surface_view);

        mid2 = new Double[Student.QUANTITY_QUESTIONS][Student.QUANTITY_OPTIONS];
        mid3 = new Double[Student.QUANTITY_QUESTIONS][Student.QUANTITY_OPTIONS];

        Button fileButton = findViewById(R.id.file);
        fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                File file = new File(Environment.getExternalStoragePublicDirectory(
//                        Environment.DIRECTORY_DOCUMENTS), "input.txt");

                String date = getDate();
                try {
                    FileOutputStream outputStream = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS), "picture" + date + ".jpg"));
                    OutputStreamWriter osw = new OutputStreamWriter(outputStream);

                    Bitmap bmp = null;

//                    Imgproc.cvtColor(input, rgb, Imgproc.COLOR_BGR2RGB);

                    try {
                        bmp = Bitmap.createBitmap(lastCameraFrame.cols(), lastCameraFrame.rows(), Bitmap.Config.ARGB_8888);
                        Utils.matToBitmap(lastCameraFrame, bmp);
                    }
                    catch (CvException e){
                        Log.d("Exception",e.getMessage());
                    }

                    bmp.compress(Bitmap.CompressFormat.PNG, 0, outputStream);

                    osw.close();
                } catch (Throwable t) {
                    Toast.makeText(getApplicationContext(),
                            "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
                }




                try {
                    FileOutputStream outputStream = new FileOutputStream(new File(Environment.getExternalStoragePublicDirectory(
                            Environment.DIRECTORY_DOCUMENTS), "data" + date + ".txt"));
                    OutputStreamWriter osw = new OutputStreamWriter(outputStream);
                    osw.write(getData());
                    osw.close();
                } catch (Throwable t) {
                    Toast.makeText(getApplicationContext(),
                            "Exception: " + t.toString(), Toast.LENGTH_LONG).show();
                }
            }
        });

        mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.tutorial1_activity_java_surface_view);

        mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);

        ANSWER[0][2] = true;
        ANSWER[1][0] = true;
        ANSWER[2][3] = true;
        ANSWER[3][3] = true;
        ANSWER[4][2] = true;
        ANSWER[5][1] = true;
        ANSWER[6][0] = true;

        mOpenCvCameraView.setCvCameraViewListener(this);
    }

    public String getDate(){
        Date currentTime = Calendar.getInstance().getTime();
        return currentTime.getYear() + "-" + currentTime.getMonth() + "-" + currentTime.getDate() + "-" + currentTime.getHours() + "-" + currentTime.getMinutes() + "-" + currentTime.getSeconds();

    }

    public String getData(){
        StringBuilder res = new StringBuilder();
        res.append(Student.QUANTITY_QUESTIONS+ " " + Student.QUANTITY_OPTIONS);
        res.append("\n");

        res.append(MAX_MID2_FOR_FALSE+" "+MAX_MID3_FOR_FALSE);
        res.append("\n");

        res.append(MIN_MID2_FOR_TRUE + " " +MAX_MID3_FOR_TRUE);
        res.append("\n");

        for(int i = 0; i < RESULT.length; i++){
            for(int j = 0; j < RESULT[i].length; j++){
                res.append(!RESULT[i][j] + " ");
            }
            res.append("\n");
        }
        res.append("\n");

        for(int i = 0; i < ANSWER.length; i++){
            for(int j = 0; j < ANSWER[i].length; j++){
                res.append(ANSWER[i][j] + " ");
            }
            res.append("\n");
        }
        res.append("\n");

        for(int i = 0; i < mid2.length; i++){
            for(int j = 0; j < mid2[i].length; j++){
                res.append(mid2[i][j] + " ");
            }
            res.append("\n");
        }
        res.append("\n");

        for(int i = 0; i < mid3.length; i++){
            for(int j = 0; j < mid3[i].length; j++){
                res.append(mid3[i][j] + " ");
            }
            res.append("\n");
        }
        res.append("\n");

        return res.toString();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (!OpenCVLoader.initDebug()) {
            Log.d(TAG, "Internal OpenCV library not found. Using OpenCV Manager for initialization");
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_0_0, this, mLoaderCallback);
        } else {
            Log.d(TAG, "OpenCV library found inside package. Using it!");
            mLoaderCallback.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        if (mOpenCvCameraView != null)
            mOpenCvCameraView.disableView();
    }

    public void onCameraViewStarted(int width, int height) {
    }

    public void onCameraViewStopped() {
    }

    public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        mGray = inputFrame.gray();
        lastCameraFrame = inputFrame.rgba().clone();

        int h = (int) mRgba.size().height-290;     //display.getHeight();
        int w = (int) mRgba.size().width;    //display.getWidth();
//        Display display = getWindowManager().getDefaultDisplay();
//        mRgba = midRect(mRgba, mGray, new Point(w / 2, h / 2));
        for (int i = 0; i < Student.QUANTITY_QUESTIONS; i++) {
            for (int j = 0; j < Student.QUANTITY_OPTIONS; j++) {
                if (Student.QUANTITY_QUESTIONS > 1 && Student.QUANTITY_OPTIONS > 1) {
                    int l_x = (int) (1.0 * w / (Student.QUANTITY_QUESTIONS));
                    int l_y = (int) (1.0 * h / (Student.QUANTITY_OPTIONS));
                    mRgba = midRect(mRgba, mGray, new Point(l_x/2 + i * l_x, l_y/2 + j * l_y), i, j);
                } else if (Student.QUANTITY_QUESTIONS > 1) {
                    int l_x = (int) (1.0 * w / (Student.QUANTITY_QUESTIONS));
                    mRgba = midRect(mRgba, mGray, new Point(l_x/2 + i * l_x, h / 2), i, j);
                } else if (Student.QUANTITY_OPTIONS > 1) {
                    int l_y = (int) (1.0 * h / (Student.QUANTITY_OPTIONS));
                    mRgba = midRect(mRgba, mGray, new Point(w / 2, l_y/2 + j * l_y), i, j);
                } else if (Student.QUANTITY_OPTIONS == 1 && Student.QUANTITY_QUESTIONS == 1) {
                    mRgba = midRect(mRgba, mGray, new Point(w / 2, h / 2), i, j);
                }
            }
            TextView text = findViewById(R.id.text);

//            text.setText(Integer.toString(Student.students.size()));
//            text.setText(Integer.toString(Student.students.size()));
        }

        SUM_RESULT = 0;
        for(int i = 0; i < Student.QUANTITY_QUESTIONS; i++){
            if(isTrue(i)){
                SUM_RESULT++;
            }
        }

        MARK = ((SUM_RESULT/Student.QUANTITY_QUESTIONS)*4 + 1);

        Imgproc.putText(mRgba, Integer.toString(MARK)+" / " + SUM_RESULT,new Point(w-100, h+200), 3, 0.8, new Scalar(Color.red(0), Color.green(0), Color.blue(0)));

        return mRgba;
    }

    private Mat midRect(Mat mRgba, Mat mGray, Point centr, int i, int j) {
//        Display display = getWindowManager().getDefaultDisplay();
        int h = (int) mRgba.size().height;     //display.getHeight();
        int w = (int) mRgba.size().width;    //display.getWidth();
        int side = (int) (w * SIDE_OF_SQUARE);
        Point leftRight = new Point(centr.x - side / 2, centr.y - side / 2);
        Point rightLeft = new Point(centr.x + side / 2, centr.y + side / 2);
//        Imgproc.line(mRgba, new Point(0, 3 * h / 7), new Point(w, 3 * h / 7), new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
//        Imgproc.line(mRgba, new Point(0, 4 * h / 7), new Point(w, 4 * h / 7), new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
//        Imgproc.line(mRgba, new Point((w / 2) - h / 14, 0), new Point((w / 2) - h / 14, h), new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
//        Imgproc.line(mRgba, new Point((w / 2) + h / 14, 0), new Point((w / 2) + h / 14, h), new Scalar(Color.red(0), Color.green(0), Color.blue(0)));


        Rect rect = new Rect(leftRight, rightLeft);
//        int sum = 0;
//        for (int i = rect.x; i < rect.x + rect.width; i++) {
//            for (int j = rect.y; j < rect.y + rect.height; j++) {
//                if (mGray.get(j, i) != null) {
//                    sum += (int) mGray.get(j, i)[0];
//                }
//            }
//        }
//
//        MID1 = (1.0 * sum) / (rect.height * rect.height);
        MatOfDouble mean = new MatOfDouble(0, 0, 0, 0);
        MatOfDouble stddev = new MatOfDouble(0, 0, 0, 0);
//        MatOfByte mask = new MatOfByte(w, h);
//        for(int i = (int) leftRight.x; i < leftRight.x + w*SIDE_OF_SQUARE; i++){
//            for(int j = (int) leftRight.y; j < leftRight.y + w*SIDE_OF_SQUARE; j++){
//                byte[] arr = new byte[1];
//                arr[0] = 1;
//                mask.get(i, j, arr);
//            }
//        }
        Core.meanStdDev(mGray.submat(rect), mean, stddev/*, mask*/);
        mid2[i][j] = mean.toArray()[0];
        mid3[i][j] = stddev.toArray()[0];
//        Imgproc.putText(mRgba, toShortString(MID1, 7), new Point(leftRight.x, leftRight.y + 15), 3, 0.7, new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
        Scalar colorOfRect = new Scalar(Color.red(235), Color.green(255), Color.blue(0));
        if(mid2[i][j] < MAX_MID2_FOR_FALSE && mid3[i][j] < MAX_MID3_FOR_FALSE){
            colorOfRect = new Scalar(33, 255, 0);
            Imgproc.putText(mRgba, "false", rightLeft, 3, 0.5, new Scalar(0, 0, 0));
            RESULT[i][j] = false;
        }else if(mid2[i][j] > MIN_MID2_FOR_TRUE && mid3[i][j] < MAX_MID3_FOR_TRUE){
            colorOfRect = new Scalar(255, 12, 0);
            Imgproc.putText(mRgba, "true", rightLeft, 3, 0.5, new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
            RESULT[i][j] = true;
        }else{
            Imgproc.putText(mRgba, "not found", rightLeft, 3, 0.5, new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
            RESULT[i][j] = false;
        }
        Imgproc.rectangle(mRgba, leftRight, rightLeft, colorOfRect);
        Imgproc.putText(mRgba, String.format("%05.2f", mid2[i][j]), new Point(leftRight.x, leftRight.y), 3, 0.5, new Scalar(Color.red(0), Color.green(0), Color.blue(0)));
        Imgproc.putText(mRgba, String.format("%05.2f", mid3[i][j]), new Point(leftRight.x, leftRight.y + rect.height + 10), 3, 0.5, new Scalar(Color.red(0), Color.green(0), Color.blue(0)));

        return mRgba;
    }

    private boolean isTrue(int numberOfQuestion){
        for(int i = 0; i < Student.QUANTITY_OPTIONS;i++){
            if(ANSWER[numberOfQuestion][i] != RESULT[numberOfQuestion][i]){
                return false;
            }
        }
        return true;
    }

    private String toShortString(Double d, int leangth) {
        String res = Double.toString(d);
        while (res.length() < leangth + 1) {
            res += "0";
        }
        return res.substring(0, leangth);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        Log.i(TAG, "called onCreateOptionsMenu");
        mItemSendResult = menu.add("Send Result");
        mItemClean = menu.add("Clean");
        mItemSaveAnswer = menu.add("Save Answer");
        mItemCheckStudent = menu.add("Next Student");

        return true;
    }

    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(TAG, "called onOptionsItemSelected; selected item: " + item);

        if (item == mItemSendResult) {
            new Thread(new Runnable(){
                @Override
                public void run() {
                    final String username = getString(R.string.email);
                    final String password = getString(R.string.password);

                    Properties props = new Properties();
                    props.put("mail.smtp.auth", "true");
                    props.put("mail.smtp.starttls.enable", "true");
                    props.put("mail.smtp.host", "smtp.gmail.com");
                    props.put("mail.smtp.port", "587");

                    Session session = Session.getInstance(props,
                            new Authenticator() {
                                protected PasswordAuthentication getPasswordAuthentication() {
                                    return new PasswordAuthentication(username, password);
                                }
                            });

                    try {

                        Message message = new MimeMessage(session);
                        message.setFrom(new InternetAddress("practice.PTHS@gmail.com"));
                        message.setRecipients(javax.mail.Message.RecipientType.TO,
                                InternetAddress.parse("polzikd@mail.ru"));
                        message.setSubject("Testing Subject");
                        message.setText(message());

                        Transport.send(message);

                        Log.i("Done", "Email was sended");

                    } catch (MessagingException e) {
                        throw new RuntimeException(e);
                    }            }
            }).start();
        } else if (item == mItemClean) {
            Student.students = new ArrayList<Student>();
        } else if (item == mItemSaveAnswer) {
            for (int i = 0; i < RESULT.length; i++) {
                ANSWER[i] = Arrays.copyOf(RESULT[i], RESULT.length);//copyOf(RESULT[i], RESULT[i].length);
            }
        }else if (item == mItemCheckStudent) {
            lastStudent.newID();
            if(lastStudent.isInhere()){
                Toast.makeText(getApplicationContext(), "Вы уже проверяли этого ученика", Toast.LENGTH_SHORT);
            }else {
                Student.students.add(lastStudent.clone());
            }

        }

        return true;
    }

    public static String message(){
        String message = "";
        for(Student s:Student.students){
            message += s.message();
        }
        return message;
    }
}