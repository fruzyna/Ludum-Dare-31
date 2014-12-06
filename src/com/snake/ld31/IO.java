package com.snake.ld31;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;

public class IO
{
	static String saveDir = "save";
	
	public static File[] getSaves()
	{
		File dir = new File(saveDir);
		return dir.listFiles();
	}
	
	public static void loadConfig()
	{
	}
	
	public static void loadSave(File file)
	{
		JSONObject jsave = new JSONObject(readFromFile(file));
		DataContainer.worldName = jsave.getString("world-name");
		DataContainer.worldHeight = jsave.getInt("world-height");
		DataContainer.worldWidth = jsave.getInt("world-width");
		JSONArray jrooms = jsave.getJSONArray("rooms");
		for(int i = 0; i < jrooms.length(); i++)
		{
			JSONObject jroom = jrooms.getJSONObject(i);
			DataContainer.rooms[jroom.getInt("room-x")][jroom.getInt("room-y")] = new Room(RoomType.values()[jroom.getInt("room-type")]);
		}
		Main.instance.startGame();
	}
	
	public static void saveConfig()
	{
	}
	
	public static void saveSave()
	{
		JSONObject jsave = new JSONObject();
		jsave.put("world-name", DataContainer.worldName);
		jsave.put("world-height", DataContainer.worldHeight);
		jsave.put("world-width", DataContainer.worldWidth);
		JSONArray jrooms = new JSONArray();
		for(int i = 0; i < DataContainer.rooms.length; i++)
		{
			for(int j = 0; j < DataContainer.rooms[i].length; j++)
			{
				JSONObject jroom = new JSONObject();
				jroom.put("room-type", DataContainer.rooms[i][j].getRoomType().ordinal());
				jroom.put("room-x", i);
				jroom.put("room-y", j);
				jrooms.put(jroom);
			}
		}
		jsave.put("rooms", jrooms);
		try
		{
			File saveFile = new File("save" + File.separator + DataContainer.worldName);
			saveFile.getParentFile().mkdirs();
			if(!saveFile.exists()) {
				saveFile.createNewFile();
			}
			BufferedWriter bw = new BufferedWriter(new FileWriter(saveFile));
			bw.write(jsave.toString());
			bw.close();
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
    //gets a string of data from the save file
    public static String readFromFile(File file)
    {
        if (!file.exists())
        {
            try
            {
                file.createNewFile();
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }
        StringBuilder sb = new StringBuilder();
        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            
            while ((line = br.readLine()) != null)
            {
                sb.append(line);
            }
            
            br.close( );
            
            return sb.toString();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return "";
    }
}
