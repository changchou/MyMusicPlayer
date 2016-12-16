package com.jikexueyuan.mymusicplayer;

import android.content.Context;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Administrator on 2015/12/28 0028.
 */
public class LrcProcess {

    private LrcContent mLrcContent;
    private List<LrcContent> lrcList;

    public LrcProcess() {
        mLrcContent = new LrcContent();
        lrcList = new ArrayList<LrcContent>();
    }

    public List<LrcContent> getLrcList() {
        return lrcList;
    }


    public void readLRC(Context context,int rawId) {
        try {

            InputStream inputStream = context.getResources().openRawResource(rawId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "utf-8");
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                System.out.println(line);
                line = line.replace("[", "");
                line = line.replace("]", "@");
                String lrcLine[] = line.split("@");
                String lrcLineContent = lrcLine[lrcLine.length - 1];
                Pattern pattern = Pattern.compile("\\d{2}");
                for (int i = 0; i < lrcLine.length - 1; i++) {
                    String lrcLineTime = lrcLine[i];
                    lrcLineTime = lrcLineTime.replace(":", ".");
                    lrcLineTime = lrcLineTime.replace(".", "@");
                    String timeData[] = lrcLineTime.split("@");
                    Matcher matcher = pattern.matcher(timeData[0]);
                    if (timeData.length == 3 && matcher.matches()) {
                        int m = Integer.parseInt(timeData[0]);
                        int s = Integer.parseInt(timeData[1]);
                        int ms = Integer.parseInt(timeData[2]);
                        int time = (m * 60 + s) * 1000 + ms * 10;
                        mLrcContent.setLrcStr(lrcLineContent);
                        mLrcContent.setLrcTime(time);
                        lrcList.add(mLrcContent);
                        mLrcContent = new LrcContent();
                    }
                }

            }
            bufferedReader.close();
            inputStreamReader.close();
            inputStream.close();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}


