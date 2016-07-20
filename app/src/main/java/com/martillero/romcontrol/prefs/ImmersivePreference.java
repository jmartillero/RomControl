package com.martillero.romcontrol.prefs;

        import android.app.AlertDialog;
        import android.content.ComponentName;
        import android.content.Context;
        import android.content.DialogInterface;
        import android.content.Intent;
        import android.content.pm.ActivityInfo;
        import android.content.pm.PackageManager;
        import android.content.pm.ResolveInfo;
        import android.content.res.Resources;
        import android.graphics.drawable.Drawable;
        import android.os.AsyncTask;
        import android.os.Bundle;
        import android.preference.DialogPreference;
        import android.support.v7.widget.LinearLayoutManager;
        import android.support.v7.widget.RecyclerView;
        import android.util.AttributeSet;
        import android.util.Log;
        import android.util.TypedValue;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.Button;
        import android.widget.CheckBox;
        import android.widget.CompoundButton;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.Spinner;
        import android.widget.TextView;

        import com.martillero.romcontrol.R;

        import java.util.ArrayList;
        import java.util.Collections;
        import java.util.Comparator;
        import java.util.List;
        import java.util.Objects;

/* Created by Roberto Mariani and Anna Berkovitch, 20/03/16
This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program. If not, see <http://www.gnu.org/licenses/>.*/
public class ImmersivePreference extends DialogPreference {
    Context c;
    PackageManager pm;
    String LOG_TAG ="list_log";
    private List<AppInfo> mList;
    private AsyncTask<Void, Void, Void> mAppLoader;
    private RecyclerView recyclerView;
    private List<String> mPersistedPackagesList;
    private ProgressBar mProgressBar;
    Spinner mSpinner;

    public ImmersivePreference(Context context, AttributeSet attrs) {
        super(context, attrs);
        c = context;
        pm = c.getPackageManager();
        setDialogLayoutResource(R.layout.immersive_list);
    }


    private List<AppInfo> getAppList() {
        List<AppInfo> list = new ArrayList<>();
        Intent intent = new Intent(Intent.ACTION_MAIN, null);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        List<ResolveInfo> resolveInfos = pm.queryIntentActivities(intent, 0);
        String persistedString = getPersistedString("");
        Log.d(LOG_TAG, "getAppList persisted string is " + persistedString);
        if (persistedString != null) {
            String[] packagesArray = persistedString.split(";-");
            Collections.addAll(mPersistedPackagesList, packagesArray);
        }
        for (int i = 0; i < resolveInfos.size(); i++) {
            AppInfo appInfo = new AppInfo();
            appInfo.appIcon = resolveInfos.get(i).activityInfo.loadIcon(pm);
            appInfo.appLabel = resolveInfos.get(i).loadLabel(pm).toString();
            appInfo.appPackage = resolveInfos.get(i).activityInfo.packageName;
            appInfo.isSelected = mPersistedPackagesList.contains(appInfo.appPackage );
            ActivityInfo ai = resolveInfos.get(i).activityInfo;

            ComponentName name = new ComponentName(ai.applicationInfo.packageName,
                    ai.name);
            Intent launchIntent = new Intent(Intent.ACTION_MAIN);
            launchIntent.addCategory(Intent.CATEGORY_LAUNCHER);
            launchIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            launchIntent.setComponent(name);
            appInfo.intent = launchIntent;
            list.add(appInfo);
        }
        return list;
    }



    public class AppInfo {
        String appLabel;
        String appPackage;
        Drawable appIcon;
        Intent intent;
        boolean isSelected;
    }

    protected void showDialog(Bundle state) {
        super.showDialog(state);
        TypedValue typedValue = new TypedValue();
        Resources.Theme theme = getContext().getTheme();
        theme.resolveAttribute(R.attr.colorAccent, typedValue, true);
        AlertDialog dialog = (AlertDialog) getDialog();
        dialog.show();
        Button cancel = dialog.getButton(AlertDialog.BUTTON_NEGATIVE);
        Button ok = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
        cancel.setTextColor(typedValue.data);
        ok.setTextColor(typedValue.data);
        dialog.getWindow().setBackgroundDrawableResource(R .drawable.dialog_bg);
    }

    public void onClick(final DialogInterface dialog, int which) {
        if (which == AlertDialog.BUTTON_POSITIVE) {
            StringBuilder sb = new StringBuilder();
            if (mSpinner.getSelectedItemPosition() != 0){
                sb.append(getCurrentSpinnerValue());
                Log.d(LOG_TAG, "onClick triggered by positive button");
                for (String value : mPersistedPackagesList) {
                    if (!value.contains("immersive")){
                        Log.d(LOG_TAG, "onClick value is " + value);
                        if (!value.equals("")) {
                            sb.append(";-").append(value);
                        }
                    }
                }} else {
                sb.append(getCurrentSpinnerValue());
            }
            persistString(sb.toString());
            Log.d(LOG_TAG, "onClick persisted string is " + sb.toString());
        }
    }

    private String getCurrentSpinnerValue(){
        String s ="";
        int i = mSpinner.getSelectedItemPosition();
        if (i == 0) {
            s = "immersive.full";
        } else if (i == 1) {
            s = "immersive.full=*";
        } else if(i == 2){
            s = "immersive.navigation=*";
        } else if(i == 3){
            s = "immersive.status=*";
        }

        return s;
    }

    public int getSavedSpinnerValue() {
        int i = 0;
        String sub;
        String value = getPersistedString("");
        if (!Objects.equals(value, "")) {
            if(value.contains("=")){
                Log.d("spinner", "string value is " + value);
                if (value.contains(";")){
                    sub = value.substring(0, value.indexOf(";"));
                }else {
                    sub = value;
                }

                if (Objects.equals(sub, "immersive.full=*")) {

                    i = 1;
                } else if (Objects.equals(sub, "immersive.navigation=*")) {
                    i = 2;
                } else if (Objects.equals(sub, "immersive.status=*")) {
                    i = 3;
                }
                Log.d("spinner", "position value is " + i);
            } else {
                Log.d("spinner", "string value is " + value);
                Log.d("spinner", "saved position is " + i);
                i = 0;
            }} return i;
    }


    protected void onBindDialogView(View view) {
        super.onBindDialogView(view);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mPersistedPackagesList = new ArrayList<>();
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar2);
        mSpinner = (Spinner) view.findViewById(R.id.spinner);
        mSpinner.setSelection(getSavedSpinnerValue());
        loadApps();
    }



    private void loadApps() {
        mAppLoader = new AsyncTask<Void, Void, Void>() {

            protected void onPreExecute() {
                mProgressBar.setVisibility(View.VISIBLE);
            }

            protected Void doInBackground(Void... params) {
                mList = getAppList();
                Collections.sort(mList, new Comparator<AppInfo>() {

                    public int compare(AppInfo lhs, AppInfo rhs) {
                        return String.CASE_INSENSITIVE_ORDER.compare(lhs.appLabel , rhs.appLabel);
                    }
                });
                return null;
            }

            protected void onPostExecute(Void aVoid) {
                mProgressBar.setVisibility(View.GONE);
                LinearLayoutManager llm = new LinearLayoutManager(c);
                recyclerView.setLayoutManager(llm);
                AppAdapter appAdapter = new AppAdapter();
                recyclerView.setAdapter(appAdapter);
            }
        }.execute();
    }

    public class AppAdapter extends RecyclerView.Adapter<AppAdapter.ViewHolder> {


        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R .layout.row_item, parent, false);
            final ViewHolder vh = new ViewHolder(v);
            v.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    vh.checkBox.setChecked(!vh.checkBox.isChecked());
                }
            });
            return vh;
        }

        public void onBindViewHolder(ViewHolder holder, final int position) {
            holder.appName.setText(mList.get(position).appLabel);
            holder.appIcon.setImageDrawable(mList.get(position ).appIcon);
            holder.checkBox.setFocusable(false);
            holder.checkBox.setOnCheckedChangeListener(null);
            holder.checkBox.setChecked(mList.get(position).isSelected);
            holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mPersistedPackagesList.add(mList.get(position).appPackage);
                    } else {
                        mPersistedPackagesList.remove(mList.get(position). appPackage);
                    }
                    mList.get(position).isSelected = isChecked;

                }
            });
        }


        public int getItemCount() {
            if (mList != null) {
                return mList.size();

            } else {
                return 0;
            }
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            CheckBox checkBox;
            ImageView appIcon;
            TextView appName;

            public ViewHolder(View itemView) {
                super(itemView);
                checkBox = (CheckBox) itemView.findViewById(R.id.checkBox);
                appIcon = (ImageView) itemView.findViewById(R.id.appIcon);
                appName = (TextView) itemView.findViewById(R.id.appName);
            }
        }
    }

    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (mAppLoader != null && mAppLoader.getStatus() == AsyncTask.Status.RUNNING) {
            mAppLoader.cancel(true);
            mAppLoader = null;
        }
    }
}

