package com.pbsi2.fakenewsbaby;/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * BadNews represents an news item. It holds the details
 * of that event such as title, as well as time.
 */
public class BadNews {

    /**
     * Title of the article
     */
    public final String title;

    /**
     * Section of the article
     */
    public final String section;

    /**
     * Author of the article
     */
    public final String author;
    /**
     * Author of the article
     */
    public final String link;
    /**
     * Author of the article
     */
    public final String type;
    /**
     * Tdate
     */
    public final String date;


    /**
     * Constructs a new {@link BadNews}.
     *
     * @param title   is the title o
     * @param section is the section
     * @param author  is whether or not a author is listed
     * @param link    is the link tp the source
     * @param type is the description
     * @param date    is ,well, the date
     */
    public BadNews(String title, String section, String link, String author, String type, String date) {
        this.title = title;
        this.section = section;
        this.author = author;
        this.link = link;
        this.type = type;
        this.date = date;

    }

    public String getTitle() {
        return title;
    }
    public String getSection() {
        return section;
    }
    public String getAuthor() {
        return author;
    }
    public String getLink() {
        return link;
    }
    public String getType() {
        return type;
    }
    public String getDate() {
        return date;
    }

}
