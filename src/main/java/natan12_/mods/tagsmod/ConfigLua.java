package natan12_.mods.tagsmod;

import org.luaj.vm2.LuaTable;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class ConfigLua
{
    HashMap<String, String> map = new HashMap<String, String>();
    File config;

    public ConfigLua(File configfile, String filename)
    {
        if(configfile == null || filename == null) throw new RuntimeException(new IllegalArgumentException("ConfigLua: either configfile or filename are null"));
        config = new File(configfile.getParentFile(), filename);
        if(!config.exists())
        {
            try
            {
                if(config.createNewFile())
                    save();
            }catch(Exception e)
            {
                System.err.println("[TagsMod/ConfigLua] Error creating server commands file");
                e.printStackTrace();
            }
        }
        load();
    }

    public void save()
    {
        try
        {
            FileOutputStream fos = new FileOutputStream(config);
            StringBuilder builder = new StringBuilder();
            builder.append("{");
            for(Map.Entry<String, String> entry : map.entrySet())
            {
                builder.append("['");
                builder.append(entry.getKey());
                builder.append("'] = '");
                builder.append(entry.getValue());
                builder.append("', ");
            }
            builder.append("}");
            fos.write(builder.toString().getBytes());
            fos.close();
        }
        catch(Throwable e)
        {
            System.err.println("[TagsMod/ConfigLua] Error saving configs");
            printStackStace(e);
        }
    }

    public void load()
    {
        try
        {
            FileInputStream fis = new FileInputStream(config);
            BufferedReader reader = new BufferedReader(new InputStreamReader(fis));
            String line = null;
            StringBuilder builder = new StringBuilder();
            while((line = reader.readLine()) != null)
            {
                builder.append(line);
                builder.append("\n");
            }
            fis.close();
            reader.close();
            System.out.println("[TagsMod/ConfigLua] Loaded following config file:\n" + builder.toString());
            LuaTable table = (LuaTable) JsePlatform.standardGlobals().load("return " + builder.toString()).call();
            for(LuaValue key : table.keys())
            {
                map.put(key.tojstring(), table.get(key).tojstring());
            }
        }
        catch(Throwable e)
        {
            System.err.println("[TagsMod/ConfigLua] Error loading configs");
            printStackStace(e);
        }
    }

    public void printStackStace(Throwable th)
    {
        StringBuilder sb = new StringBuilder();
        sb.append(th.getClass().getCanonicalName());
        sb.append(": ");
        sb.append(th.getMessage());
        for(StackTraceElement element : th.getStackTrace())
        {
            boolean isNative = element.isNativeMethod();
            sb.append(" at ")
            .append(element.getClassName())
            .append(".")
            .append(element.getMethodName())
            .append(isNative ? "(Native method)" : "(" + element.getFileName()+ ":" + element.getLineNumber() + ")")
            .append("\n");
        }
        System.err.println(sb.toString());
    }

    public String list()
    {
        StringBuilder sb = new StringBuilder();
        for(Map.Entry<String, String> entry : map.entrySet())
        {
            sb.append(entry.getKey());
            sb.append(" -> ");
            sb.append(entry.getValue());
            sb.append(" \n");
        }
        String s = sb.toString();
        if(s.length() == 0)
            return "No data added";
        return s.substring(0, s.length()-2);
    }

    public void set(String key, String value)
    {
        map.put(key, value);
    }

    public void remove(String key)
    {
        map.remove(key);
    }

    public boolean containsKey(String s)
    {
        return map.containsKey(s);
    }

    public Set<String> getKeys()
    {
        return map.keySet();
    }

    public String[] get(String key, String split)
    {
        String value = map.get(key);
        if(value == null) return null;
        if(split == null) return new String[]{value};
        return value.split(split);
    }
}
