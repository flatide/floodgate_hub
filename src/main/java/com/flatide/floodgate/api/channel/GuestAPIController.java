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

package com.flatide.floodgate.api.channel;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.util.*;

import com.flatide.floodgate.agent.ChannelAgent;
import com.flatide.floodgate.agent.Context;
import com.flatide.floodgate.agent.flow.stream.FGInputStream;
import com.flatide.floodgate.agent.flow.stream.FGSharableInputStream;
import com.flatide.floodgate.agent.flow.stream.carrier.container.JSONContainer;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping(path="/api")
public class GuestAPIController {
    static List<byte[]> holder = new ArrayList<>();
    @PostMapping(path="/{api}")
    public @ResponseBody Map postFlow(
        @RequestBody Map<String, Object> data,
        @PathVariable String api,
        @RequestParam Map<String, String> params) throws Exception {
        MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
        long fullused = mbean.getHeapMemoryUsage().getUsed();
        long fullmax = mbean.getHeapMemoryUsage().getMax();
        long fullfree = fullmax - fullused;

        System.out.println(String.format("max: %d, used: %d, free %d", fullmax, fullused, fullfree));

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            //if ("client_ip".equals(header) || "content-type".equals(header) || "content-length".equals(header) || "transfer-encoding".equals(header)) {
                System.out.println(header + " : " + request.getHeader(header));
            //}
        }
        System.out.println();

        FGInputStream current = new FGSharableInputStream(new JSONContainer(data, "HEADER", "ITEMS"));

        ChannelAgent agent = new ChannelAgent();
        agent.addContext(Context.CONTEXT_KEY.REQUEST_PARAMS, params);
        agent.addContext(Context.CONTEXT_KEY.REQUEST_BODY, data);
        return agent.process(current, api);
    }

    @GetMapping(path="/{api}")
    public @ResponseBody Map get(
        @RequestBody Map<String, Object> data,
        @PathVariable String api,
        @RequestParam Map<String, String> params) throws Exception {
        MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
        long fullused = mbean.getHeapMemoryUsage().getUsed();
        long fullmax = mbean.getHeapMemoryUsage().getMax();
        long fullfree = fullmax - fullused;

        System.out.println(String.format("max: %d, used: %d, free %d", fullmax, fullused, fullfree));
        
        Map result = new HashMap<String, byte[]>();

        return result;
    }

    
    @GetMapping(path="/{guest}/{api}/{resource}")
    public @ResponseBody Map getFlow(
        @PathVariable String guest,
        @PathVariable String api,
        @PathVariable String resource,
        @RequestParam Map<String, String> params) throws Exception {
        MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
        long fullused = mbean.getHeapMemoryUsage().getUsed();
        long fullmax = mbean.getHeapMemoryUsage().getMax();
        long fullfree = fullmax - fullused;

        System.out.println(String.format("max: %d, used: %d, free %d", fullmax, fullused, fullfree));
        
        Map data = new HashMap<>();
        data.put("ITEMS", new ArrayList<>());

        FGInputStream stream = new FGSharableInputStream(new JSONContainer(data, "HEADER", "ITEMS"));

        ChannelAgent agent = new ChannelAgent();
        agent.addContext(Context.CONTEXT_KEY.REQUEST_PARAMS, params);
        agent.addContext(Context.CONTEXT_KEY.REQUEST_BODY, data);
        return agent.process(stream, "/" + guest + "/" + api + "/" + resource);
    }

    @PostMapping(path="/{guest}/{api}/{resource}")
    public @ResponseBody Map postFlow(
        @RequestBody Map<String, Object> data,
        @PathVariable String guest,
        @PathVariable String api,
        @PathVariable String resource,
        @RequestParam Map<String, String> params) throws Exception {
        MemoryMXBean mbean = ManagementFactory.getMemoryMXBean();
        long fullused = mbean.getHeapMemoryUsage().getUsed();
        long fullmax = mbean.getHeapMemoryUsage().getMax();
        long fullfree = fullmax - fullused;

        System.out.println(String.format("max: %d, used: %d, free %d", fullmax, fullused, fullfree));

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        Enumeration<String> headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String header = headers.nextElement();
            //if ("client_ip".equals(header) || "content-type".equals(header) || "content-length".equals(header) || "transfer-encoding".equals(header)) {
                System.out.println(header + " : " + request.getHeader(header));
            //}
        }
        System.out.println();
        
        FGInputStream current = new FGSharableInputStream(new JSONContainer(data, "HEADER", "ITEMS"));

        ChannelAgent agent = new ChannelAgent();
        agent.addContext(Context.CONTEXT_KEY.REQUEST_PARAMS, params);
        agent.addContext(Context.CONTEXT_KEY.REQUEST_BODY, data);
        return agent.process(current, "/" + guest + "/" + api + "/" + resource);
    }
 }
