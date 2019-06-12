package com.akigo.core.message;

import java.util.*;

public class MergeResourceBundle {


    protected Map<String, Object> resourceMap = new Hashtable<>();


    protected List<String> resourceNames = new ArrayList<>();

    public void addResource(final ResourceBundle resource) {
        if (resource == null) {
            return;
        }

        resourceNames.add(resource.getBaseBundleName());
        for (String key : resource.keySet()) {
            resourceMap.put(key, resource.getObject(key));
        }
    }

    public boolean containsKey(final String key) {
        return resourceMap.containsKey(key);
    }

    protected Object handleGetObject(final String key) {
        return resourceMap.get(key);
    }

    public Enumeration<String> getKeys() {
        return Collections.enumeration(resourceMap.keySet());
    }

    public boolean contains(String bundleName) {

        return this.resourceNames.contains(bundleName);
    }

    public String get(String messageID) {

        String ret = null;
        if (messageID != null && resourceMap.containsKey(messageID)) {
            return (String) resourceMap.get(messageID);
        }
        return ret;
    }
}
