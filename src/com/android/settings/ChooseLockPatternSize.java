/*
 * Copyright (C) 2010 The Android Open Source Project
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

package com.android.settings;

import android.content.Intent;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;

import com.android.internal.widget.LockPatternUtils;

public class ChooseLockPatternSize extends PreferenceActivity {

    @Override
    public Intent getIntent() {
        Intent modIntent = new Intent(super.getIntent());
        modIntent.putExtra(EXTRA_SHOW_FRAGMENT, ChooseLockPatternSizeFragment.class.getName());
        modIntent.putExtra(EXTRA_NO_HEADERS, true);
        return modIntent;
    }

    public static class ChooseLockPatternSizeFragment extends SettingsPreferenceFragment {
        private ChooseLockSettingsHelper mChooseLockSettingsHelper;

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            mChooseLockSettingsHelper = new ChooseLockSettingsHelper(this.getActivity());
            addPreferencesFromResource(R.xml.security_settings_pattern_size);
        }

        @Override
        public boolean onPreferenceTreeClick(PreferenceScreen preferenceScreen,
                Preference preference) {
            final String key = preference.getKey();

            int PATTERN_SIZE = 3;
            if ("lock_pattern_size_3".equals(key)) {
                PATTERN_SIZE = 3;
            } else if ("lock_pattern_size_4".equals(key)) {
                PATTERN_SIZE = 4;
            } else if ("lock_pattern_size_5".equals(key)) {
                PATTERN_SIZE = 5;
            } else if ("lock_pattern_size_6".equals(key)) {
                PATTERN_SIZE = 6;
            }

            final boolean isFallback = getActivity().getIntent()
                .getBooleanExtra(LockPatternUtils.LOCKSCREEN_BIOMETRIC_WEAK_FALLBACK, false);
            boolean showTutorial = !mChooseLockSettingsHelper.utils().isPatternEverChosen();

            Intent intent = new Intent();
            intent.setClass(getActivity(), showTutorial
                    ? ChooseLockPatternTutorial.class
                    : ChooseLockPattern.class);
            intent.putExtra("pattern_size", PATTERN_SIZE);
            intent.putExtra("key_lock_method", "pattern");
            intent.putExtra("confirm_credentials", false);
            intent.putExtra(LockPatternUtils.LOCKSCREEN_BIOMETRIC_WEAK_FALLBACK,
                    isFallback);
            intent.addFlags(Intent.FLAG_ACTIVITY_FORWARD_RESULT);
                    startActivity(intent);

            finish();
            return true;
        }
    }
}
