package com.pbsi2.fakenewsbaby;

public class GlobalVar {

    /**
     * URL of the article
     */
    public final String pUrl;


    /**
     * Constructs a new {@link com.pbsi2.fakenewsbaby.BadNews}.
     *
     * @param pUrl is the title o
     */
    public GlobalVar(String pUrl) {
        this.pUrl = pUrl;

    }

    public String getUrl() {
        return pUrl;
    }

}

