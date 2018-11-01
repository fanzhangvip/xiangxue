package com.me94me.example_navigatioin_resource.navigation;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * NavDeepLink encapsulates the parsing and matching of a navigation deep link.
 */
class NavDeepLink {
    private static final Pattern SCHEME_PATTERN = Pattern.compile("^(\\w+-)*\\w+:");

    private final ArrayList<String> mArguments = new ArrayList<>();
    private final Pattern mPattern;

    /**
     * NavDestinations should be created via {@link Navigator#createDestination}.
     */
    NavDeepLink(@NonNull String uri) {
        StringBuffer uriRegex = new StringBuffer("^");

        if (!SCHEME_PATTERN.matcher(uri).find()) {
            uriRegex.append("http[s]?://");
        }
        Pattern fillInPattern = Pattern.compile("\\{(.+?)\\}");
        Matcher matcher = fillInPattern.matcher(uri);
        while (matcher.find()) {
            String argName = matcher.group(1);
            mArguments.add(argName);
            matcher.appendReplacement(uriRegex, "");
            uriRegex.append("(.+?)");
        }
        matcher.appendTail(uriRegex);
        mPattern = Pattern.compile(uriRegex.toString());
    }

    boolean matches(@NonNull Uri deepLink) {
        return mPattern.matcher(deepLink.toString()).matches();
    }

    @Nullable
    Bundle getMatchingArguments(@NonNull Uri deepLink) {
        Matcher matcher = mPattern.matcher(deepLink.toString());
        if (!matcher.matches()) {
            return null;
        }
        Bundle bundle = new Bundle();
        int size = mArguments.size();
        for (int index = 0; index < size; index++) {
            String argument = mArguments.get(index);
            bundle.putString(argument, matcher.group(index + 1));
        }
        return bundle;
    }
}
