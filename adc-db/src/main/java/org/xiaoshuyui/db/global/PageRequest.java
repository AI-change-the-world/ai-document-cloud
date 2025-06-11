package org.xiaoshuyui.db.global;

import java.util.Map;

import lombok.Data;

@Data
public class PageRequest {
    Map<String, Object> params;
    int pageId;
    int pageSize;
}
