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

import com.flatide.floodgate.agent.meta.MetaManager;
import com.flatide.floodgate.agent.meta.MetaTable;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path="/system")
public class MetaController {
    @GetMapping(path="/meta")
    public @ResponseBody Map get(
            @RequestParam String table, 
            @RequestParam(required = false) String id) throws Exception {
        try {
            if (id == null) {
                MetaTable metaTable = MetaManager.shared().getTable(table);
                if (metaTable == null) {
                    return null;
                }
                return metaTable.getRows();
            } else {
                return MetaManager.shared().read(table, id);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PostMapping(path="/meta")
    public @ResponseBody Map post(
            @RequestBody Map<String, Object> data,
            @RequestParam String table,
            @RequestParam String key) throws Exception {
        try {
            MetaManager.shared().insert(table, key, data, true);
            
            Map<String, Object> result = new HashMap<>();
            result.put("result", "Ok");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @PutMapping(path="/meta")
    public @ResponseBody Map put(
            @RequestBody Map<String, Object> data,
            @RequestParam String table,
            @RequestParam String key) throws Exception {
        try {
            MetaManager.shared().update(table, key, data, true);

            Map<String, Object> result = new HashMap<>();
            result.put("result", "Ok");
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }
}
