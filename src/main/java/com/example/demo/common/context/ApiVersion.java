package com.example.demo.common.context;

import com.sunnysuperman.commons.util.VersionUtil;

public class ApiVersion {
    public static final ApiVersion V1_0 = parse("1.0");
    public static final ApiVersion LATEST = V1_0;

    private long longValue;

    private ApiVersion(String s) {
        longValue = VersionUtil.parseVersionAsLong(s, 3, 3);
    }

    public static ApiVersion parse(String s) {
        return new ApiVersion(s);
    }

    public static ApiVersion getLatest() {
        return LATEST;
    }

    public long longValue() {
        return longValue;
    }

    public boolean isGreaterThanOrEqual(ApiVersion another) {
        return longValue >= another.longValue;
    }

    public boolean isLessThan(ApiVersion another) {
        return longValue < another.longValue;
    }
}
