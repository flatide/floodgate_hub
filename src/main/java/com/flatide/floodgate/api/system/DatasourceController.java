/*
 * MIT License
 *
 * Copyright (c) 2022 FLATIDE LC.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.flatide.floodgate.api.system;

import com.flatide.floodgate.ConfigurationManager;
import com.flatide.floodgate.FloodgateConstants;
import com.flatide.floodgate.agent.meta.MetaManager;
import com.flatide.floodgate.system.security.FloodgateSecurity;

import org.springframework.web.bind.annotation.*;
import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path="/system")
public class DatasourceController {
    @GetMapping(path="/datasource")
    public @ResponseBody List get(
            @RequestParam(required = false) String id,
            @RequestParam(required = false, defaultValue = "1") int from,
            @RequestParam(required = false, defaultValue = "-1") int to) throws Exception {
        try {
            return MetaManager.shared().readList(ConfigurationManager.shared().getString(FloodgateConstants.META_SOURCE_TABLE_FOR_DATASOURCE), id);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping(path="/datasource")
    public @ResponseBody Map post(
        @RequestBody Map<String, Object> data ) throws Exception {
        try {
            Map<String, Object> d = (Map) data.get("DATA");
            for (Map.Entry<String, Object> e : d.entrySet()) {
                String key = (String) e.getKey();
                if ("PASSWORD".equals(key)) {
                    String value = (String) e.getValue();
                    String encrypted = FloodgateSecurity.shared().encrypt(value);
                    e.setValue(encrypted);
                }
            }

            long cur = System.currentTimeMillis();
            Timestamp current = new Timestamp(cur);

            data.put("CREATE_DATE", current);
            data.put("MODIFY_DATE", current);
            MetaManager.shared().insert(ConfigurationManager.shared().getString(FloodgateConstants.META_SOURCE_TABLE_FOR_DATASOURCE), "ID", data, true);

            Map<String, Object> result = new HashMap<>();
            result.put("result", "Ok");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping(path="/datasource")
    public @ResponseBody Map put(
            @RequestBody Map<String, Object> data) throws Exception {
        try {
            Map old = MetaManager.shared().read(ConfigurationManager.shared().getString(FloodgateConstants.META_SOURCE_TABLE_FOR_DATASOURCE), (String) data.get("ID"));
            
            old.put("TABLE_NAME", ConfigurationManager.shared().getString(FloodgateConstants.META_SOURCE_TABLE_FOR_DATASOURCE));

            MetaManager.shared().insert(ConfigurationManager.shared().getString(FloodgateConstants.META_SOURCE_TABLE_FOR_META_HISTORY), "ID", old, true);
            Map<String, Object> d = (Map) data.get("DATA");
            for (Map.Entry<String, Object> e : d.entrySet()) {
                String key = (String) e.getKey();
                if ("PASSWORD".equals(key)) {
                    String value = (String) e.getValue();
                    String encrypted = FloodgateSecurity.shared().encrypt(value);
                    e.setValue(encrypted);
                }
            }

            long cur = System.currentTimeMillis();
            Timestamp current = new Timestamp(cur);
            data.remove("CREATE_DATE");
            data.put("MODIFY_DATE", current);
            MetaManager.shared().update(ConfigurationManager.shared().getString(FloodgateConstants.META_SOURCE_TABLE_FOR_DATASOURCE), "ID", data, true);

            Map<String, Object> result = new HashMap<>();
            result.put("result", "Ok");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @DeleteMapping(path="/datasource")
    public @ResponseBody Map delete(
            @RequestParam(required = true) String id) throws Exception {
        try {
            Map old = MetaManager.shared().read(ConfigurationManager.shared().getString(FloodgateConstants.META_SOURCE_TABLE_FOR_DATASOURCE), id);
            

            old.put("TABLE_NAME", ConfigurationManager.shared().getString(FloodgateConstants.META_SOURCE_TABLE_FOR_DATASOURCE));

            MetaManager.shared().insert(ConfigurationManager.shared().getString(FloodgateConstants.META_SOURCE_TABLE_FOR_META_HISTORY), "ID", old, true);

            MetaManager.shared().delete(ConfigurationManager.shared().getString(FloodgateConstants.META_SOURCE_TABLE_FOR_DATASOURCE), id, true);
            
            Map<String, Object> result = new HashMap<>();
            result.put("result", "Ok");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}

