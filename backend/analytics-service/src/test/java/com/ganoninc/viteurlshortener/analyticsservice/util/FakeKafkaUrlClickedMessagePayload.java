package com.ganoninc.viteurlshortener.analyticsservice.util;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;

public class FakeKafkaUrlClickedMessagePayload {
    public static JSONObject getFakePayload() throws JSONException {
        JSONObject payload = new JSONObject();
        payload.put("shortId", "abcd");
        payload.put("timestamp", Instant.now().toString());
        payload.put("ip", "127.0.0.1");
        payload.put("userAgent", "Mozilla/5.0");

        return payload;
    }

    public static JSONObject getFakeIncompletePayload() throws JSONException {
        JSONObject incompletePayload = new JSONObject();
        incompletePayload.put("shortId", "abcd");

        return incompletePayload;
    }
}
