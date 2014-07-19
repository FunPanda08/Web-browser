/*
 * Copyright 2014 A.C.R. Development
 */
package acr.browser.lightning;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import net.fast.web.browser.R;

public class SettingsActivity extends Activity {

	private static int API = android.os.Build.VERSION.SDK_INT;
	private static SharedPreferences.Editor mEditPrefs;
	private static int mAgentChoice;
	private static String mHomepage;
	private static TextView mAgentTextView;
	private static TextView mDownloadTextView;
	private static int mEasterEggCounter = 0;
	private static String mSearchUrl;
	private static String mDownloadLocation;
	private static TextView mHomepageText;
	private static SharedPreferences mPreferences;
	private static TextView mSearchText;
	private Context mContext;
	private Activity mActivity;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.settings);
		mContext = this;
		mActivity = this;
		init();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		finish();
		return true;
	}

	@SuppressLint("NewApi")
	public void init() {
		// mPreferences storage
		getActionBar().setHomeButtonEnabled(true);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		mPreferences = getSharedPreferences(PreferenceConstants.PREFERENCES, 0);
		if (mPreferences.getBoolean(PreferenceConstants.HIDE_STATUS_BAR, false)) {
			getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
					WindowManager.LayoutParams.FLAG_FULLSCREEN);
		}

		mEditPrefs = mPreferences.edit();

		// initialize UI
		RelativeLayout layoutLocation = (RelativeLayout) findViewById(R.id.layoutLocation);
		RelativeLayout layoutFullScreen = (RelativeLayout) findViewById(R.id.layoutFullScreen);
		RelativeLayout layoutFlash = (RelativeLayout) findViewById(R.id.layoutFlash);
		RelativeLayout layoutBlockAds = (RelativeLayout) findViewById(R.id.layoutAdBlock);

		mSearchText = (TextView) findViewById(R.id.searchText);

		switch (mPreferences.getInt(PreferenceConstants.SEARCH, 1)) {
		case 0:
			mSearchText.setText(getResources().getString(R.string.custom_url));
			break;
		case 1:
			mSearchText.setText("Google");
			break;
		case 2:
			mSearchText.setText("Android Search");
			break;
		case 3:
			mSearchText.setText("Bing");
			break;
		case 4:
			mSearchText.setText("Yahoo");
			break;
		case 5:
			mSearchText.setText("StartPage");
			break;
		case 6:
			mSearchText.setText("StartPage (Mobile)");
			break;
		case 7:
			mSearchText.setText("DuckDuckGo");
			break;
		case 8:
			mSearchText.setText("DuckDuckGo Lite");
			break;
		case 9:
			mSearchText.setText("Baidu");
			break;
		case 10:
			mSearchText.setText("Yandex");
		}

		mAgentTextView = (TextView) findViewById(R.id.agentText);
		mHomepageText = (TextView) findViewById(R.id.homepageText);
		mDownloadTextView = (TextView) findViewById(R.id.downloadText);
		if (API >= 19) {
			mEditPrefs.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT, 0);
			mEditPrefs.commit();
		}
		boolean locationBool = mPreferences.getBoolean(PreferenceConstants.LOCATION, false);
		int flashNum = mPreferences.getInt(PreferenceConstants.ADOBE_FLASH_SUPPORT, 0);
		boolean fullScreenBool = mPreferences.getBoolean(PreferenceConstants.FULL_SCREEN, false);
		mAgentChoice = mPreferences.getInt(PreferenceConstants.USER_AGENT, 1);
		mHomepage = mPreferences.getString(PreferenceConstants.HOMEPAGE, Constants.HOMEPAGE);
		mDownloadLocation = mPreferences.getString(PreferenceConstants.DOWNLOAD_DIRECTORY,
				Environment.DIRECTORY_DOWNLOADS);

		mDownloadTextView.setText(Constants.EXTERNAL_STORAGE + "/" + mDownloadLocation);

		String code = "HOLO";

		try {
			PackageInfo p = getPackageManager().getPackageInfo(
					getPackageName(), 0);
			code = p.versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		TextView version = (TextView) findViewById(R.id.versionCode);
		version.setText(code + "");

		if (mHomepage.contains("about:home")) {
			mHomepageText.setText(getResources().getString(
					R.string.action_homepage));
		} else if (mHomepage.contains("about:blank")) {
			mHomepageText.setText(getResources()
					.getString(R.string.action_blank));
		} else if (mHomepage.contains("about:bookmarks")) {
			mHomepageText.setText(getResources().getString(
					R.string.action_bookmarks));
		} else {
			mHomepageText.setText(mHomepage);
		}

		switch (mAgentChoice) {
		case 1:
			mAgentTextView.setText(getResources().getString(R.string.agent_default));
			break;
		case 2:
			mAgentTextView.setText(getResources().getString(R.string.agent_desktop));
			break;
		case 3:
			mAgentTextView.setText(getResources().getString(R.string.agent_mobile));
			break;
		case 4:
			mAgentTextView.setText(getResources().getString(R.string.agent_custom));
		}
		RelativeLayout r1, r2, r3, r4, licenses;
		r1 = (RelativeLayout) findViewById(R.id.setR1);
		r2 = (RelativeLayout) findViewById(R.id.setR2);
		r3 = (RelativeLayout) findViewById(R.id.setR3);
		r4 = (RelativeLayout) findViewById(R.id.setR4);
		licenses = (RelativeLayout) findViewById(R.id.layoutLicense);
		
		licenses.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				//NOTE: In order to comply with the open source license,
				//it is advised that you leave this code so that the License
				//Activity may be viewed by the user.
				startActivity(new Intent(Constants.LICENSE_INTENT));
			}
			
		});
		
		Switch location = new Switch(this);
		Switch fullScreen = new Switch(this);
		Switch flash = new Switch(this);
		Switch adblock = new Switch(this);

		r1.addView(location);
		r2.addView(fullScreen);
		r3.addView(flash);
		r4.addView(adblock);
		location.setChecked(locationBool);
		fullScreen.setChecked(fullScreenBool);
		if (flashNum > 0) {
			flash.setChecked(true);
		} else {
			flash.setChecked(false);
		}
		adblock.setChecked(mPreferences.getBoolean(PreferenceConstants.BLOCK_ADS, false));

		initSwitch(location, fullScreen, flash, adblock);
		clickListenerForSwitches(layoutLocation, layoutFullScreen, layoutFlash, layoutBlockAds,
				location, fullScreen, flash, adblock);

		RelativeLayout agent = (RelativeLayout) findViewById(R.id.layoutUserAgent);
		RelativeLayout download = (RelativeLayout) findViewById(R.id.layoutDownload);
		RelativeLayout homepage = (RelativeLayout) findViewById(R.id.layoutHomepage);
		RelativeLayout advanced = (RelativeLayout) findViewById(R.id.layoutAdvanced);
		RelativeLayout source = (RelativeLayout) findViewById(R.id.layoutSource);

		agent(agent);
		download(download);
		homepage(homepage);
		advanced(advanced);
		source(source);
		search();
		easterEgg();
	}

	public void search() {
		RelativeLayout search = (RelativeLayout) findViewById(R.id.layoutSearch);
		search.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder picker = new AlertDialog.Builder(mActivity);
				picker.setTitle(getResources().getString(
						R.string.title_search_engine));
				CharSequence[] chars = {
						getResources().getString(R.string.custom_url),
						"Google", "Android Search", "Bing", "Yahoo", "StartPage", "StartPage (Mobile)", 
						"DuckDuckGo (Privacy)", "DuckDuckGo Lite (Privacy)", "Baidu (Chinese)",
						"Yandex (Russian)"  };

				int n = mPreferences.getInt(PreferenceConstants.SEARCH, 1);

				picker.setSingleChoiceItems(chars, n,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mEditPrefs.putInt(PreferenceConstants.SEARCH, which);
								mEditPrefs.commit();
								switch (which) {
								case 0:
									searchUrlPicker();
									break;
								case 1:
									mSearchText.setText("Google");
									break;
								case 2:
									mSearchText.setText("Android Search");
									break;
								case 3:
									mSearchText.setText("Bing");
									break;
								case 4:
									mSearchText.setText("Yahoo");
									break;
								case 5:
									mSearchText.setText("StartPage");
									break;
								case 6:
									mSearchText.setText("StartPage (Mobile)");
									break;
								case 7:
									mSearchText.setText("DuckDuckGo");
									break;
								case 8:
									mSearchText.setText("DuckDuckGo Lite");
									break;
								case 9:
									mSearchText.setText("Baidu");
									break;
								case 10:
									mSearchText.setText("Yandex");
								}
							}
						});
				picker.setNeutralButton(
						getResources().getString(R.string.action_ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				picker.show();
			}

		});
	}

	public void searchUrlPicker() {
		final AlertDialog.Builder urlPicker = new AlertDialog.Builder(
				SettingsActivity.this);

		urlPicker.setTitle(getResources().getString(R.string.custom_url));
		final EditText getSearchUrl = new EditText(SettingsActivity.this);

		mSearchUrl = mPreferences
				.getString(PreferenceConstants.SEARCH_URL, Constants.GOOGLE_SEARCH);
		getSearchUrl.setText(mSearchUrl);
		urlPicker.setView(getSearchUrl);
		urlPicker.setPositiveButton(getResources()
				.getString(R.string.action_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = getSearchUrl.getText().toString();
						mEditPrefs.putString(PreferenceConstants.SEARCH_URL, text);
						mEditPrefs.commit();
						mSearchText.setText(getResources().getString(
								R.string.custom_url)
								+ ": " + text);
					}
				});
		urlPicker.show();
	}

	public void clickListenerForSwitches(RelativeLayout one,
			RelativeLayout two, RelativeLayout three, RelativeLayout layoutBlockAds, final Switch loc,
			final Switch full, final Switch flash, final Switch adblock) {
		layoutBlockAds.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View v) {
				adblock.setChecked(!adblock.isChecked());
			}
			
		});
		one.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				loc.setChecked(!loc.isChecked());
			}

		});
		two.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				full.setChecked(!full.isChecked());
			}

		});
		three.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (API < 19) {
					flash.setChecked(!flash.isChecked());
				} else {
					Utils.createInformativeDialog(mContext, getResources()
							.getString(R.string.title_warning), getResources()
							.getString(R.string.dialog_adobe_dead));
				}
			}

		});
	}

	public void easterEgg() {
		RelativeLayout easter = (RelativeLayout) findViewById(R.id.layoutVersion);
		easter.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mEasterEggCounter++;
				if (mEasterEggCounter == 10) {
					startActivity(new Intent(Intent.ACTION_VIEW, Uri
							.parse("http://imgs.xkcd.com/comics/compiling.png")));
					finish();
					mEasterEggCounter = 0;
				}
			}

		});
	}

	public void initSwitch(Switch location, Switch fullscreen, Switch flash, Switch adblock) {
		adblock.setOnCheckedChangeListener(new OnCheckedChangeListener(){

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mEditPrefs.putBoolean(PreferenceConstants.BLOCK_ADS, isChecked);
				mEditPrefs.commit();
			}
			
		});
		location.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mEditPrefs.putBoolean(PreferenceConstants.LOCATION, isChecked);
				mEditPrefs.commit();

			}

		});
		flash.setEnabled(API < 19);
		flash.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					getFlashChoice();
				} else {
					mEditPrefs.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT, 0);
					mEditPrefs.commit();
				}

				boolean flashInstalled = false;
				try {
					PackageManager pm = getPackageManager();
					ApplicationInfo ai = pm.getApplicationInfo(
							"com.adobe.flashplayer", 0);
					if (ai != null)
						flashInstalled = true;
				} catch (NameNotFoundException e) {
					flashInstalled = false;
				}
				if (!flashInstalled && isChecked) {
					Utils.createInformativeDialog(
							SettingsActivity.this,
							getResources().getString(R.string.title_warning),
							getResources().getString(
									R.string.dialog_adobe_not_installed));
					buttonView.setChecked(false);
					mEditPrefs.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT, 0);
					mEditPrefs.commit();

				} else if ((API >= 17) && isChecked) {
					Utils.createInformativeDialog(
							SettingsActivity.this,
							getResources().getString(R.string.title_warning),
							getResources().getString(
									R.string.dialog_adobe_unsupported));
				}
			}

		});
		fullscreen.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mEditPrefs.putBoolean(PreferenceConstants.FULL_SCREEN, isChecked);
				mEditPrefs.commit();

			}

		});
	}

	private void getFlashChoice() {
		AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
		builder.setTitle(mContext.getResources()
				.getString(R.string.title_flash));
		builder.setMessage(getResources().getString(R.string.flash))
				.setCancelable(true)
				.setPositiveButton(
						getResources().getString(R.string.action_manual),
						new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int id) {
								mEditPrefs.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT, 1);
								mEditPrefs.commit();
							}
						})
				.setNegativeButton(
						getResources().getString(R.string.action_auto),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mEditPrefs.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT, 2);
								mEditPrefs.commit();
							}
						}).setOnCancelListener(new OnCancelListener() {

					@Override
					public void onCancel(DialogInterface dialog) {
						mEditPrefs.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT, 0);
						mEditPrefs.commit();
					}

				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	public void initCheckBox(CheckBox location, CheckBox fullscreen,
			CheckBox flash) {
		location.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mEditPrefs.putBoolean(PreferenceConstants.LOCATION, isChecked);
				mEditPrefs.commit();

			}

		});
		flash.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				int n = 0;
				if (isChecked) {
					n = 1;
				}
				mEditPrefs.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT, n);
				mEditPrefs.commit();
				boolean flashInstalled = false;
				try {
					PackageManager pm = getPackageManager();
					ApplicationInfo ai = pm.getApplicationInfo(
							"com.adobe.flashplayer", 0);
					if (ai != null)
						flashInstalled = true;
				} catch (NameNotFoundException e) {
					flashInstalled = false;
				}
				if (!flashInstalled && isChecked) {
					Utils.createInformativeDialog(
							SettingsActivity.this,
							getResources().getString(R.string.title_warning),
							getResources().getString(
									R.string.dialog_adobe_not_installed));
					buttonView.setChecked(false);
					mEditPrefs.putInt(PreferenceConstants.ADOBE_FLASH_SUPPORT, 0);
					mEditPrefs.commit();

				} else if ((API > 17) && isChecked) {
					Utils.createInformativeDialog(
							SettingsActivity.this,
							getResources().getString(R.string.title_warning),
							getResources().getString(
									R.string.dialog_adobe_unsupported));
				}
			}

		});
		fullscreen.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				mEditPrefs.putBoolean(PreferenceConstants.FULL_SCREEN, isChecked);
				mEditPrefs.commit();

			}

		});
	}

	public void agent(RelativeLayout view) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder agentPicker = new AlertDialog.Builder(
						mActivity);
				agentPicker.setTitle(getResources().getString(
						R.string.title_user_agent));
				CharSequence[] chars = {
						getResources().getString(R.string.agent_default),
						getResources().getString(R.string.agent_desktop),
						getResources().getString(R.string.agent_mobile),
						getResources().getString(R.string.agent_custom) };
				mAgentChoice = mPreferences.getInt(PreferenceConstants.USER_AGENT, 1);
				agentPicker.setSingleChoiceItems(chars, mAgentChoice - 1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								mEditPrefs.putInt(PreferenceConstants.USER_AGENT, which + 1);
								mEditPrefs.commit();
								switch (which + 1) {
								case 1:
									mAgentTextView.setText(getResources().getString(
											R.string.agent_default));
									break;
								case 2:
									mAgentTextView.setText(getResources().getString(
											R.string.agent_desktop));
									break;
								case 3:
									mAgentTextView.setText(getResources().getString(
											R.string.agent_mobile));
									break;
								case 4:
									mAgentTextView.setText(getResources().getString(
											R.string.agent_custom));
									agentPicker();
									break;
								}
							}
						});
				agentPicker.setNeutralButton(
						getResources().getString(R.string.action_ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {
								// TODO Auto-generated method stub

							}

						});
				agentPicker
						.setOnCancelListener(new DialogInterface.OnCancelListener() {

							@Override
							public void onCancel(DialogInterface dialog) {
								// TODO Auto-generated method stub
								Log.i("Cancelled", "");
							}
						});
				agentPicker.show();

			}

		});
	}

	public void agentPicker() {
		final AlertDialog.Builder agentStringPicker = new AlertDialog.Builder(
				mActivity);

		agentStringPicker.setTitle(getResources().getString(
				R.string.title_user_agent));
		final EditText getAgent = new EditText(SettingsActivity.this);
		agentStringPicker.setView(getAgent);
		agentStringPicker.setPositiveButton(
				getResources().getString(R.string.action_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = getAgent.getText().toString();
						mEditPrefs.putString(PreferenceConstants.USER_AGENT_STRING, text);
						mEditPrefs.commit();
						mAgentTextView.setText(getResources().getString(
								R.string.agent_custom));
					}
				});
		agentStringPicker.show();
	}

	public void download(RelativeLayout view) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				AlertDialog.Builder picker = new AlertDialog.Builder(mActivity);
				picker.setTitle(getResources().getString(
						R.string.title_download_location));
				CharSequence[] chars = {
						getResources().getString(R.string.agent_default),
						getResources().getString(R.string.agent_custom) };
				mDownloadLocation = mPreferences.getString(PreferenceConstants.DOWNLOAD_DIRECTORY,
						Environment.DIRECTORY_DOWNLOADS);
				int n = -1;
				if (mDownloadLocation.contains(Environment.DIRECTORY_DOWNLOADS)) {
					n = 1;
				} else {
					n = 2;
				}

				picker.setSingleChoiceItems(chars, n - 1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								switch (which + 1) {
								case 1:
									mEditPrefs.putString(PreferenceConstants.DOWNLOAD_DIRECTORY,
											Environment.DIRECTORY_DOWNLOADS);
									mEditPrefs.commit();
									mDownloadTextView.setText(Constants.EXTERNAL_STORAGE
											+ "/"
											+ Environment.DIRECTORY_DOWNLOADS);
									break;
								case 2:
									downPicker();

									break;
								}
							}
						});
				picker.setNeutralButton(
						getResources().getString(R.string.action_ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				picker.show();
			}

		});
	}

	public void homePicker() {
		final AlertDialog.Builder homePicker = new AlertDialog.Builder(
				mActivity);
		homePicker.setTitle(getResources().getString(
				R.string.title_custom_homepage));
		final EditText getHome = new EditText(SettingsActivity.this);
		mHomepage = mPreferences.getString(PreferenceConstants.HOMEPAGE, Constants.HOMEPAGE);
		if (!mHomepage.startsWith("about:")) {
			getHome.setText(mHomepage);
		} else {
			getHome.setText("http://www.google.com");
		}
		homePicker.setView(getHome);
		homePicker.setPositiveButton(
				getResources().getString(R.string.action_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = getHome.getText().toString();
						mEditPrefs.putString(PreferenceConstants.HOMEPAGE, text);
						mEditPrefs.commit();
						mHomepageText.setText(text);
					}
				});
		homePicker.show();
	}

	@SuppressWarnings("deprecation")
	public void downPicker() {
		final AlertDialog.Builder downLocationPicker = new AlertDialog.Builder(
				mActivity);
		LinearLayout layout = new LinearLayout(this);
		downLocationPicker.setTitle(getResources().getString(
				R.string.title_download_location));
		final EditText getDownload = new EditText(SettingsActivity.this);
		getDownload.setBackgroundResource(0);
		mDownloadLocation = mPreferences.getString(PreferenceConstants.DOWNLOAD_DIRECTORY,
				Environment.DIRECTORY_DOWNLOADS);
		int padding = Utils.convertToDensityPixels(this, 10);

		LinearLayout.LayoutParams lparams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT);

		getDownload.setLayoutParams(lparams);
		getDownload.setTextColor(Color.DKGRAY);
		getDownload.setText(mDownloadLocation);
		getDownload.setPadding(0, padding, padding, padding);

		TextView v = new TextView(this);
		v.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		v.setTextColor(Color.DKGRAY);
		v.setText(Constants.EXTERNAL_STORAGE + "/");
		v.setPadding(padding, padding, 0, padding);
		layout.addView(v);
		layout.addView(getDownload);
		if (API < 16) {
			layout.setBackgroundDrawable(getResources().getDrawable(
					android.R.drawable.edit_text));
		} else {
			layout.setBackground(getResources().getDrawable(
					android.R.drawable.edit_text));
		}
		downLocationPicker.setView(layout);
		downLocationPicker.setPositiveButton(
				getResources().getString(R.string.action_ok),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						String text = getDownload.getText().toString();
						mEditPrefs.putString(PreferenceConstants.DOWNLOAD_DIRECTORY, text);
						mEditPrefs.commit();
						mDownloadTextView.setText(Constants.EXTERNAL_STORAGE + "/"
								+ text);
					}
				});
		downLocationPicker.show();
	}

	public void homepage(RelativeLayout view) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				AlertDialog.Builder picker = new AlertDialog.Builder(mActivity);
				picker.setTitle(getResources().getString(R.string.home));
				CharSequence[] chars = {
						getResources().getString(R.string.action_homepage),
						getResources().getString(R.string.action_blank),
						getResources().getString(R.string.action_bookmarks),
						getResources().getString(R.string.action_webpage) };
				mHomepage = mPreferences.getString(PreferenceConstants.HOMEPAGE, Constants.HOMEPAGE);
				int n = -1;
				if (mHomepage.contains("about:home")) {
					n = 1;
				} else if (mHomepage.contains("about:blank")) {
					n = 2;
				} else if (mHomepage.contains("about:bookmarks")) {
					n = 3;
				} else {
					n = 4;
				}

				picker.setSingleChoiceItems(chars, n - 1,
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

								switch (which + 1) {
								case 1:
									mEditPrefs.putString(PreferenceConstants.HOMEPAGE, "about:home");
									mEditPrefs.commit();
									mHomepageText
											.setText(getResources().getString(
													R.string.action_homepage));
									break;
								case 2:
									mEditPrefs.putString(PreferenceConstants.HOMEPAGE, "about:blank");
									mEditPrefs.commit();
									mHomepageText.setText(getResources()
											.getString(R.string.action_blank));
									break;
								case 3:
									mEditPrefs.putString(PreferenceConstants.HOMEPAGE,
											"about:bookmarks");
									mEditPrefs.commit();
									mHomepageText.setText(getResources()
											.getString(
													R.string.action_bookmarks));

									break;
								case 4:
									homePicker();

									break;
								}
							}
						});
				picker.setNeutralButton(
						getResources().getString(R.string.action_ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog,
									int which) {

							}
						});
				picker.show();
			}

		});
	}

	public void advanced(RelativeLayout view) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Constants.ADVANCED_SETTINGS_INTENT));
			}

		});
	}

	public void source(RelativeLayout view) {
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri
						.parse("http://twitter.com/ACRDevelopment")));
				finish();
			}

		});
	}
}
