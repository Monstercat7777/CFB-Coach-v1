package antdroid.cfbcoach;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import Simulation.League;

public class RecruitingActivity extends AppCompatActivity {

    League simLeague;

    // Variables use during recruiting
    private String teamName;
    private String teamAbbr;
    private int recruitingBudget;
    Random rand = new Random();
    private int HCtalent;
    private int ratingTolerance;
    private int tolerance;
    private int min;
    private int max;


    private ArrayList<String> playersRecruited;
    private ArrayList<String> playersRedshirted;
    private ArrayList<String> playersGraduating;
    private ArrayList<String> teamQBs;
    private ArrayList<String> teamRBs;
    private ArrayList<String> teamWRs;
    private ArrayList<String> teamTEs;
    private ArrayList<String> teamOLs;
    private ArrayList<String> teamKs;
    private ArrayList<String> teamDLs;
    private ArrayList<String> teamLBs;
    private ArrayList<String> teamCBs;
    private ArrayList<String> teamSs;

    private ArrayList<String> teamPlayers; //all players

    private ArrayList<String> availQBs;
    private ArrayList<String> availRBs;
    private ArrayList<String> availWRs;
    private ArrayList<String> availTEs;
    private ArrayList<String> availOLs;
    private ArrayList<String> availKs;
    private ArrayList<String> availDLs;
    private ArrayList<String> availLBs;
    private ArrayList<String> availCBs;
    private ArrayList<String> availSs;
    private ArrayList<String> availAll;
    private ArrayList<String> avail50;

    private ArrayList<String> west;
    private ArrayList<String> midwest;
    private ArrayList<String> central;
    private ArrayList<String> east;

    private ArrayList<String> recruitDisplay;

    private int needQBs;
    private int needRBs;
    private int needWRs;
    private int needTEs;
    private int needOLs;
    private int needKs;
    private int needDLs;
    private int needLBs;
    private int needCBs;
    private int needSs;

    int minPlayers = 50;
    int minQBs = 3;
    int minRBs = 6;
    int minWRs = 8;
    int minTEs = 3;
    int minOLs = 12;
    int minKs = 2;
    int minDLs = 10;
    int minLBs = 8;
    int minCBs = 8;
    int minSs = 4;

    int redshirtCount = 0;
    int maxRedshirt = 6;
    int maxPlayers = 70;

    int five = 84;
    int four = 78;
    int three = 68;
    int two = 58;

    int height;
    int weight;

    // Whether to show pop ups every recruit
    private boolean showPopUp;

    // Keep track of which position is selected in spinner
    private String currentPosition;

    // Android Components to keep track of
    private TextView budgetText;
    private Spinner positionSpinner;
    private ExpandableListView recruitList;
    private ArrayList<String> positions;
    private ArrayAdapter dataAdapterPosition;
    private ExpandableListAdapterRecruiting expListAdapter;
    private Map<String, List<String>> playersInfo;
    private List<String> players;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recruiting);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Init all the ArrayLists
        playersRecruited = new ArrayList<String>();
        playersRedshirted = new ArrayList<String>();
        playersGraduating = new ArrayList<String>();
        teamQBs = new ArrayList<String>();
        teamRBs = new ArrayList<String>();
        teamWRs = new ArrayList<String>();
        teamTEs = new ArrayList<String>();
        teamOLs = new ArrayList<String>();
        teamKs = new ArrayList<String>();
        teamDLs = new ArrayList<String>();
        teamLBs = new ArrayList<String>();
        teamCBs = new ArrayList<String>();
        teamSs = new ArrayList<String>();
        teamPlayers = new ArrayList<String>();
        availQBs = new ArrayList<String>();
        availRBs = new ArrayList<String>();
        availWRs = new ArrayList<String>();
        availTEs = new ArrayList<String>();
        availOLs = new ArrayList<String>();
        availKs = new ArrayList<String>();
        availDLs = new ArrayList<String>();
        availLBs = new ArrayList<String>();
        availCBs = new ArrayList<String>();
        availSs = new ArrayList<String>();

        avail50 = new ArrayList<String>();
        availAll = new ArrayList<String>();
        west = new ArrayList<>();
        midwest = new ArrayList<>();
        central = new ArrayList<>();
        east = new ArrayList<>();

        // Get User Team's player info and team info for recruiting
        Bundle extras = getIntent().getExtras();
        String userTeamStr = "";
        if (extras != null) {
            userTeamStr = extras.getString("USER_TEAM_INFO");
        }

        // Parse through string
        String[] lines = userTeamStr.split("%\n");
        final String[] teamInfo = lines[0].split(",");
        teamName = teamInfo[1];
        teamAbbr = teamInfo[2];
        recruitingBudget = Integer.parseInt(teamInfo[3]) * 15;
        if (teamInfo[4].isEmpty()) {
            HCtalent = 70;
        } else {
            HCtalent = Integer.parseInt(teamInfo[4]);
        }
        getSupportActionBar().setTitle(teamName + " | Recruiting" );

        showPopUp = true;

        // First get user team's roster info
        String[] playerInfo;
        int i = 1;
        while (!lines[i].equals("END_TEAM_INFO")) {
            playerInfo = lines[i].split(",");
            if (playerInfo[2].equals("5")) {
                // Graduating player
                playersGraduating.add(getReadablePlayerInfo(lines[i]));
            } else {
                teamPlayers.add(getReadablePlayerInfo(lines[i]));
                if (playerInfo[0].equals("QB")) {
                    teamQBs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("RB")) {
                    teamRBs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("WR")) {
                    teamWRs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("TE")) {
                    teamTEs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("OL")) {
                    teamOLs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("K")) {
                    teamKs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("DL")) {
                    teamDLs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("LB")) {
                    teamLBs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("CB")) {
                    teamCBs.add(getReadablePlayerInfo(lines[i]));
                } else if (playerInfo[0].equals("S")) {
                    teamSs.add(getReadablePlayerInfo(lines[i]));
                }
            }
            ++i; // go to next line
        }

        // Add extra money if your team was fleeced
        int recBonus = (minPlayers - teamPlayers.size())*15;
        int coachBonus = HCtalent*3;
        recruitingBudget += recBonus + coachBonus;

        // Next get recruits info
        ++i;
        while (i < lines.length) {
            playerInfo = lines[i].split(",");
            availAll.add(lines[i]);
            if(playerInfo[3].equals("0")) {
                west.add(lines[i]);
            }
            if(playerInfo[3].equals("1")) {
                midwest.add(lines[i]);
            }
            if(playerInfo[3].equals("2")) {
                central.add(lines[i]);
            }
            if(playerInfo[3].equals("3")) {
                east.add(lines[i]);
            }

            if (playerInfo[0].equals("QB")) {
                availQBs.add(lines[i]);
            } else if (playerInfo[0].equals("RB")) {
                availRBs.add(lines[i]);
            } else if (playerInfo[0].equals("WR")) {
                availWRs.add(lines[i]);
            } else if (playerInfo[0].equals("TE")) {
                availTEs.add(lines[i]);
            } else if (playerInfo[0].equals("K")) {
                availKs.add(lines[i]);
            } else if (playerInfo[0].equals("OL")) {
                availOLs.add(lines[i]);
            } else if (playerInfo[0].equals("DL")) {
                availDLs.add(lines[i]);
            } else if (playerInfo[0].equals("LB")) {
                availLBs.add(lines[i]);
            } else if (playerInfo[0].equals("CB")) {
                availCBs.add(lines[i]);
            } else if (playerInfo[0].equals("S")) {
                availSs.add(lines[i]);
            }
            ++i;
        }


        //ratingTolerance = Math.round((140-HCtalent)/10);
        ratingTolerance = 0;
        max = ratingTolerance;
        min = -ratingTolerance;
        tolerance = rand.nextInt((max - min) + 1) + min;

        // Sort to get top 100 overall players
        Collections.sort(availAll, new PlayerRecruitStrCompOverall());
        Collections.sort(west, new PlayerRecruitStrCompOverall());
        Collections.sort(midwest, new PlayerRecruitStrCompOverall());
        Collections.sort(central, new PlayerRecruitStrCompOverall());
        Collections.sort(east, new PlayerRecruitStrCompOverall());

        avail50 = new ArrayList<String>(availAll.subList(0, 49));

        // Get needs for each position
        updatePositionNeeds();

        /*
          Assign components to private variables for easier access later
         */
        budgetText = findViewById(R.id.textRecBudget);
        String budgetStr = "Budget: $" + recruitingBudget;
        budgetText.setText(budgetStr);

        /*
          Set up spinner for examining choosing position to recruit
         */
        positionSpinner = findViewById(R.id.spinnerRec);
        positions = new ArrayList<String>();
        positions.add("Top 50 Recruits");
        positions.add("All Players");
        positions.add("QB (Need: " + needQBs + ")");
        positions.add("RB (Need: " + needRBs + ")");
        positions.add("WR (Need: " + needWRs + ")");
        positions.add("TE (Need: " + needTEs + ")");
        positions.add("OL (Need: " + needOLs + ")");
        positions.add("K (Need: " + needKs + ")");
        positions.add("DL (Need: " + needDLs + ")");
        positions.add("LB (Need: " + needLBs + ")");
        positions.add("CB (Need: " + needCBs + ")");
        positions.add("S (Need: " + needSs + ")");
        positions.add("West (" + west.size() + ")");
        positions.add("Midwest (" + midwest.size() + ")");
        positions.add("Central (" + central.size() + ")");
        positions.add("East (" + east.size() + ")");

        dataAdapterPosition = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, positions);
        dataAdapterPosition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        positionSpinner.setAdapter(dataAdapterPosition);
        positionSpinner.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    public void onItemSelected(
                            AdapterView<?> parent, View view, int position, long id) {
                        currentPosition = parent.getItemAtPosition(position).toString();
                        updateForNewPosition(position);
                    }

                    public void onNothingSelected(AdapterView<?> parent) {
                        //heh
                    }
                });

        /*
          Set up the "Done" button for returning back to MainActivity
         */
        Button doneRecrutingButton = findViewById(R.id.buttonDoneRecruiting);
        doneRecrutingButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                exitRecruiting();
            }
        });

        /*
          Set up the "Roster" button for displaying dialog of all players in roster
         */
        Button viewRosterButton = findViewById(R.id.buttonRecRoster);
        viewRosterButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Make dialog
                makeRosterDialog();
            }
        });

        /*
          Set up expandable list view
         */
        recruitList = findViewById(R.id.recruitExpandList);
        setPlayerList("QB");
        setPlayerInfoMap("QB");
        expListAdapter = new ExpandableListAdapterRecruiting(this);
        recruitList.setAdapter(expListAdapter);

        /*
          Set up "Expand All / Collapse All" button
         */
        final Button buttonExpandAll = findViewById(R.id.buttonRecruitExpandCollapse);
        buttonExpandAll.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(RecruitingActivity.this);
                builder.setTitle("Filter Recruits");
                final String[] sels = {"Expand All", "Collapse All", "Remove Unaffordable Players"};
                builder.setItems(sels, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        // Do something with the selection
                        if (item == 0) {
                            // Expand everyone
                            for (int i = 0; i < players.size(); ++i) {
                                recruitList.expandGroup(i, false);
                            }
                        } else if (item == 1) {
                            // Collapse everyone
                            for (int i = 0; i < players.size(); ++i) {
                                recruitList.collapseGroup(i);
                            }
                        } else if (item == 2) {
                            // Remove unaffordable
                            removeUnaffordable(players);
                            removeUnaffordable(avail50);
                            removeUnaffordable(availAll);
                            removeUnaffordable(availQBs);
                            removeUnaffordable(availRBs);
                            removeUnaffordable(availWRs);
                            removeUnaffordable(availTEs);
                            removeUnaffordable(availOLs);
                            removeUnaffordable(availKs);
                            removeUnaffordable(availDLs);
                            removeUnaffordable(availLBs);
                            removeUnaffordable(availCBs);
                            removeUnaffordable(availSs);
                            removeUnaffordable(west);
                            removeUnaffordable(midwest);
                            removeUnaffordable(central);
                            removeUnaffordable(east);
                            // Notify that players were removed
                            expListAdapter.notifyDataSetChanged();
                        }

                        dialog.dismiss();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
        });

    }

    public void removeUnaffordable(List<String> list) {
        int i = 0;
        while (i < list.size()) {
            if (getRecruitCost(list.get(i)) > recruitingBudget) {
                // Can't afford him
                list.remove(i);
            } else {
                ++i;
            }
        }
    }

    public void filterRegion(List<String> list, int region) {
        int i = 0;
        while (i < list.size()) {
            if (getRecruitRegion(list.get(i)) != region) {
                list.remove(i);
            } else {
                ++i;
            }
        }
        expListAdapter.notifyDataSetChanged();
    }

    /**
     * Used for parsing through string to get cost
     */
    public int getRecruitCost(String p) {
        String[] pSplit = p.split(",");
        return Integer.parseInt(pSplit[12]);
    }

    /**
     * Used for parsing through string to get region
     */
    public int getRecruitRegion(String p) {
        String[] pSplit = p.split(",");
        return Integer.parseInt(pSplit[3]);
    }

    @Override
    public void onBackPressed() {
        exitRecruiting();
    }

    public void setShowPopUp(boolean tf) {
        showPopUp = tf;
    }

    /**
     * Exit the recruiting activity. Called when the "Done" button is pressed or when user presses back button.
     */
    private void exitRecruiting() {
        StringBuilder sb = new StringBuilder();
        sb.append("Are you sure you are done recruiting? Any unfilled positions will be filled by walk-ons.\n\n");
        for (int i = 1; i < positions.size(); ++i) {
            sb.append("\t\t" + positions.get(i) + "\n");
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(RecruitingActivity.this);
        builder.setMessage(sb.toString())
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Send info about what recruits were selected back
                        Intent myIntent = new Intent(RecruitingActivity.this, MainActivity.class);
                        myIntent.putExtra("SAVE_FILE", "DONE_RECRUITING");
                        myIntent.putExtra("RECRUITS", getRecruitsStr());
                        RecruitingActivity.this.startActivity(myIntent);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    /**
     * Set current player list used by expandable list to correct position players
     */
    private void setPlayerList(String pos) {
        if (pos.equals("QB")) {
            players = availQBs;
        } else if (pos.equals("RB")) {
            players = availRBs;
        } else if (pos.equals("WR")) {
            players = availWRs;
        } else if (pos.equals("TE")) {
            players = availTEs;
        } else if (pos.equals("OL")) {
            players = availOLs;
        } else if (pos.equals("K")) {
            players = availKs;
        } else if (pos.equals("DL")) {
            players = availDLs;
        } else if (pos.equals("LB")) {
            players = availLBs;
        } else if (pos.equals("CB")) {
            players = availCBs;
        } else if (pos.equals("S")) {
            players = availSs;
        }
    }

    /**
     * Converts player string into '$500 QB A. Name, Overall: 89' or similar
     */
    private String getPlayerNameCost(String player) {
        String[] ps = player.split(",");
        return "$" + ps[12] + " " + ps[0] + " " + ps[1] + ">Grade: " + getStarGrade(ps[6]);
    }

    /**
     * Sets up map to align player's info with correct player
     */
    private void setPlayerInfoMap(String pos) {
        playersInfo = new LinkedHashMap<String, List<String>>();
        for (String p : players) {
            ArrayList<String> pInfoList = new ArrayList<String>();
            pInfoList.add(getPlayerDetails(p, pos));
            playersInfo.put(p.substring(0, p.length() - 2), pInfoList);
        }
    }

    /**
     * Converts the player string into details, i.e. Accuracy: A, Evasion: A, etc
     */
    private String getPlayerDetails(String player, String pos) {
        String[] ps = player.split(",");
        if (pos.equals("QB")) {
            return  "Region: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nPass Strength: " + getGrade(ps[13]) +
                    "\nPass Accuracy: " + getGrade(ps[14]) +
                    "\nEvasion: " + getGrade(ps[15]) +
                    "\nSpeed: " + getGrade(ps[16]);
        } else if (pos.equals("RB")) {
            return "Region: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nPower: " + getGrade(ps[13]) +
                    "\nSpeed: " + getGrade(ps[14]) +
                    "\nEvasion: " + getGrade(ps[15]) +
                    "\nCatching: " + getGrade(ps[16]);
        } else if (pos.equals("WR")) {
            return "Region: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nCatching: " + getGrade(ps[13]) +
                    "\nSpeed: " + getGrade(ps[14]) +
                    "\nEvasion: " + getGrade(ps[15]) +
                    "\nJumping: " + getGrade(ps[16]);
        } else if (pos.equals("TE")) {
            return "Region: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nCatching: " + getGrade(ps[13]) +
                    "\nRush Blk: " + getGrade(ps[14]) +
                    "\nEvasion: " + getGrade(ps[15]) +
                    "\nSpeed: " + getGrade(ps[16]);
        } else if (pos.equals("OL")) {
            return "Region: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nStrength: " + getGrade(ps[13]) +
                    "\nRush Blk: " + getGrade(ps[14]) +
                    "\nPass Blk: " + getGrade(ps[15]) +
                    "\nAwareness: " + getGrade(ps[16]);
        } else if (pos.equals("K")) {
            return  "Region: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nKick Power: " + getGrade(ps[13]) +
                    "\nAccuracy: " + getGrade(ps[14]) +
                    "\nClumsiness: " + getGrade(ps[15]) +
                    "\nPressure: " + getGrade(ps[16]);
        } else if (pos.equals("DL")) {
            return "Region: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nStrength: " + getGrade(ps[13]) +
                    "\nRun Stop: " + getGrade(ps[14]) +
                    "\nPass Press: " + getGrade(ps[15]) +
                    "\nTackling: " + getGrade(ps[16]);
        } else if (pos.equals("LB")) {
            return "Region: " + getRegion(Integer.parseInt(ps[3])) +
                    "\nCoverage: " + getGrade(ps[13]) +
                    "\nRun Stop: " + getGrade(ps[14]) +
                    "\nTackling: " + getGrade(ps[15]) +
                    "\nSpeed: " + getGrade(ps[16]);
        } else if (pos.equals("CB")) {
            return "Region: " + getRegion(Integer.parseInt(ps[3])) +

                    "\nCoverage: " + getGrade(ps[13]) +
                    "\nSpeed: " + getGrade(ps[14]) +
                    "\nTackling: " + getGrade(ps[15]) +
                    "\nJumping: " + getGrade(ps[16]);
        } else if (pos.equals("S")) {
            return "Region: " + getRegion(Integer.parseInt(ps[3])) +

                    "\nCoverage: " + getGrade(ps[13]) +
                    "\nSpeed: " + getGrade(ps[14]) +
                    "\nTackling: " + getGrade(ps[15]) +
                    "\nRun Stop: " + getGrade(ps[16]);
        }
        return "ERROR";
    }


    private String getGrade(String num) {
        int pRat = (Integer.parseInt(num));
        if (pRat > five) return "* * * * *";
        else if (pRat > four) return " * * * *";
        else if (pRat > three) return " * * * ";
        else if (pRat > two) return "  * * ";
        else return "  *  ";
    }

    private String getScoutGrade(int num) {
        int pRat = num;
        if (pRat > five) return "* * * * *";
        else if (pRat > four) return " * * * * ";
        else if (pRat > three) return " * * * ";
        else if (pRat > two) return "  * * ";
        else return "  *  ";
    }

    private String getStarGrade(String num) {
        int pRat = (Integer.parseInt(num));
        if (pRat == 5) return " * * * * *";
        if (pRat == 4) return " * * * *  ";
        if (pRat == 3) return " * * *    ";
        if (pRat == 2) return " * *      ";
        if (pRat == 1) return " *        ";

        else return  "??";
    }

    /**
     * Converts the lines from the file into readable lines
     */
    private String getReadablePlayerInfo(String p) {
        String[] pi = p.split(",");
        String improveStr = "";
        String transfer = "";
        if (pi[7].equals("true")) transfer = " (Transfer)";
        if (!playersRecruited.contains(p) && !playersRedshirted.contains(p)) {
            improveStr = "(+" + pi[13] + ")";
            return getInitialName(pi[1]) + " " + getYrStr(pi[2]) + " Overall: " + pi[12] + " " + improveStr + transfer;
        } else {
            improveStr = " (Recruit)";
            return getInitialName(pi[1]) + " " + getYrStr(pi[2]) + " Overall: " + pi[11] + " " + improveStr + transfer;
        }
    }

    /**
     * Converts the lines from the file into readable lines, without revealing potential used for displaying
     */
    private String getReadablePlayerInfoDisplay(String p) {
        String[] pi = p.split(",");
        return pi[6] + "-Star " + pi[0] + " " + getInitialName(pi[1]);
    }

    /**
     * Converts the lines from the file into readable lines, include position
     */
    private String getReadablePlayerInfoPos(String p) {
        String[] pi = p.split(",");
        return pi[0] + " " + getInitialName(pi[1]) + " " + getYrStr(pi[2]) + " " + pi[11] + " Ovr";
    }


    /**
     * Convert year from number to String, i.e. 3 -> Junior
     */
    private String getYrStr(String yr) {
        if (yr.equals("1")) {
            return "[Fr]";
        } else if (yr.equals("2")) {
            return "[So]";
        } else if (yr.equals("3")) {
            return "[Jr]";
        } else if (yr.equals("4")) {
            return "[Sr]";
        }
        return "[XX]";
    }

    /**
     * Convert full name into initial name
     */
    private String getInitialName(String name) {
        String[] names = name.split(" ");
        return names[0].substring(0, 1) + ". " + names[1];
    }

    /**
     * Called whenever new position is selected, updates all the components
     */
    private void updateForNewPosition(int position) {
        if (position > 1 && position < 12) {
            String[] splitty = currentPosition.split(" ");
            setPlayerList(splitty[0]);
            setPlayerInfoMap(splitty[0]);
            expListAdapter.notifyDataSetChanged();
        } else {
            // See top 100 recruits
            if (position == 0) {
                players = avail50;
            } else if(position == 12) {
                players = west;
            } else if(position == 13) {
                players = midwest;
            } else if(position == 14) {
                players = central;
            } else if(position == 15) {
                players = east;
            } else {
                players = availAll;
            }

            playersInfo = new LinkedHashMap<String, List<String>>();
            for (String p : players) {
                ArrayList<String> pInfoList = new ArrayList<String>();
                pInfoList.add(getPlayerDetails(p, p.split(",")[0]));
                playersInfo.put(p.substring(0, p.length() - 2), pInfoList);
            }
            expListAdapter.notifyDataSetChanged();
        }
    }

    /**
     * Update needs for each position
     */
    private void updatePositionNeeds() {
        // Get needs for each position
        needQBs = minQBs - teamQBs.size();
        needRBs = minRBs - teamRBs.size();
        needWRs = minWRs - teamWRs.size();
        needTEs = minTEs - teamTEs.size();
        needOLs = minOLs - teamOLs.size();
        needKs = minKs - teamKs.size();
        needDLs = minDLs - teamDLs.size();
        needLBs = minLBs - teamLBs.size();
        needCBs = minCBs - teamCBs.size();
        needSs = minSs - teamSs.size();

        if (dataAdapterPosition != null) {
            positions = new ArrayList<String>();
            positions.add("Top 50 Recruits");
            positions.add("All Players");
            positions.add("QB (Need: " + needQBs + ")");
            positions.add("RB (Need: " + needRBs + ")");
            positions.add("WR (Need: " + needWRs + ")");
            positions.add("TE (Need: " + needTEs + ")");
            positions.add("OL (Need: " + needOLs + ")");
            positions.add("K (Need: " + needKs + ")");
            positions.add("DL (Need: " + needDLs + ")");
            positions.add("LB (Need: " + needLBs + ")");
            positions.add("CB (Need: " + needCBs + ")");
            positions.add("S (Need: " + needSs + ")");
            positions.add("West (" + west.size() + ")");
            positions.add("Midwest (" + midwest.size() + ")");
            positions.add("Central (" + central.size() + ")");
            positions.add("East (" + east.size() + ")");

            dataAdapterPosition.clear();
            for (String p : positions) {
                dataAdapterPosition.add(p);
            }
            dataAdapterPosition.notifyDataSetChanged();
        }
    }

    /**
     * Get String of team roster, used for displaying in dialog
     */
    private String getRosterStr() {
        updatePositionNeeds();
        StringBuilder sb = new StringBuilder();
        String stbn = ""; //ST for starter, BN for bench
        String p = ""; //player string

        sb.append("QBs (Need: " + needQBs + ")\n");
        appendPlayers(sb, teamQBs, 1);

        sb.append("\nRBs (Need: " + needRBs + ")\n");
        appendPlayers(sb, teamRBs, 2);

        sb.append("\nWRs (Need: " + needWRs + ")\n");
        appendPlayers(sb, teamWRs, 3);

        sb.append("\nTEs (Need: " + needTEs + ")\n");
        appendPlayers(sb, teamTEs, 1);

        sb.append("\nOLs (Need: " + needOLs + ")\n");
        appendPlayers(sb, teamOLs, 5);

        sb.append("\nKs (Need: " + needKs + ")\n");
        appendPlayers(sb, teamKs, 1);

        sb.append("\nDLs (Need: " + needDLs + ")\n");
        appendPlayers(sb, teamDLs, 4);

        sb.append("\nLBs (Need: " + needLBs + ")\n");
        appendPlayers(sb, teamLBs, 3);

        sb.append("\nCBs (Need: " + needCBs + ")\n");
        appendPlayers(sb, teamCBs, 3);

        sb.append("\nSs (Need: " + needSs + ")\n");
        appendPlayers(sb, teamSs, 2);

        sb.append("\nRedshirted Players:\n");
        for (String rp : playersRedshirted) {
            sb.append("\t" + getReadablePlayerInfoPos(rp) + "\n");
        }

        return sb.toString();
    }

    private void appendPlayers(StringBuilder sb, ArrayList<String> players, int numStart) {
        String p, stbn;
        for (int i = 0; i < players.size(); ++i) {
            if (i > numStart - 1) stbn = "BN";
            else stbn = "ST";
            p = players.get(i);
            sb.append("\t" + stbn + " " + p + "\n");
        }
    }

    /**
     * Gets all the recruits in a string to send back to MainActivity to be added to user team
     */
    public String getRecruitsStr() {
        StringBuilder sb = new StringBuilder();

        for (String p : playersRecruited) {
            sb.append(p + "%\n");
        }
        sb.append("END_RECRUITS%\n");

        for (String rp : playersRedshirted) {
            sb.append(rp + "%\n");
        }
        sb.append("END_REDSHIRTS%\n");

        return sb.toString();
    }

    /**
     * Makes the Roster dialog for viewing who is on team
     */
    private void makeRosterDialog() {
        String rosterStr = getRosterStr();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(rosterStr)
                .setTitle(teamName + " Roster | Team Size: " + (teamPlayers.size()+playersRecruited.size()+playersRedshirted.size())  )
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Dismiss dialog
                    }
                });
        AlertDialog dialog = builder.create();
        dialog.show();
        TextView msgTxt = dialog.findViewById(android.R.id.message);
        msgTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
    }



    /**
     * Recruit player, add to correct list and remove from available players list
     */
    private void recruitPlayerDialog(String p, int pos, List<Integer> groupsExp) {
        final String player = p;
        final int groupPosition = pos;
        final List<Integer> groupsExpanded = groupsExp;
        int moneyNeeded = getRecruitCost(player);
        if (recruitingBudget >= moneyNeeded) {

            if (showPopUp) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Recruiting");
                builder.setMessage("Your team roster is at " + (teamPlayers.size()+playersRecruited.size()+playersRedshirted.size()) + " (Max: 70).\n\nAre you sure you want to recruit " + getReadablePlayerInfoDisplay(player) + " for $" + moneyNeeded + "?");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                recruitList.collapseGroup(groupPosition);
                                for (int i = groupPosition + 1; i < players.size(); ++i) {
                                    if (recruitList.isGroupExpanded(i)) {
                                        groupsExpanded.add(i);
                                    }
                                    recruitList.collapseGroup(i);
                                }

                                recruitPlayer(player);

                                expListAdapter.notifyDataSetChanged();
                                for (int group : groupsExpanded) {
                                    recruitList.expandGroup(group - 1);
                                }
                                dialog.dismiss();
                            }
                        });

                builder.setNeutralButton("Yes, Don't Show",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                recruitList.collapseGroup(groupPosition);
                                for (int i = groupPosition + 1; i < players.size(); ++i) {
                                    if (recruitList.isGroupExpanded(i)) {
                                        groupsExpanded.add(i);
                                    }
                                    recruitList.collapseGroup(i);
                                }

                                recruitPlayer(player);
                                setShowPopUp(false);

                                expListAdapter.notifyDataSetChanged();
                                for (int group : groupsExpanded) {
                                    recruitList.expandGroup(group - 1);
                                }
                                dialog.dismiss();
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // not successful
                                recruitList.collapseGroup(groupPosition);
                                for (int i = groupPosition + 1; i < players.size(); ++i) {
                                    if (recruitList.isGroupExpanded(i)) {
                                        groupsExpanded.add(i);
                                    }
                                    recruitList.collapseGroup(i);
                                }

                                recruitList.expandGroup(groupPosition);
                                expListAdapter.notifyDataSetChanged();
                                for (int group : groupsExpanded) {
                                    recruitList.expandGroup(group);
                                }
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

                TextView msgTxt = dialog.findViewById(android.R.id.message);
                msgTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            } else {
                // Don't show pop up dialog
                recruitList.collapseGroup(groupPosition);
                for (int i = groupPosition + 1; i < players.size(); ++i) {
                    if (recruitList.isGroupExpanded(i)) {
                        groupsExpanded.add(i);
                    }
                    recruitList.collapseGroup(i);
                }

                recruitPlayer(player);

                expListAdapter.notifyDataSetChanged();
                for (int group : groupsExpanded) {
                    recruitList.expandGroup(group - 1);
                }
            }

        } else {
            recruitList.collapseGroup(groupPosition);
            for (int i = groupPosition + 1; i < players.size(); ++i) {
                if (recruitList.isGroupExpanded(i)) {
                    groupsExpanded.add(i);
                }
                recruitList.collapseGroup(i);
            }
            Toast.makeText(this, "Not enough money!",
                    Toast.LENGTH_SHORT).show();
            recruitList.expandGroup(groupPosition);
            expListAdapter.notifyDataSetChanged();
            for (int group : groupsExpanded) {
                recruitList.expandGroup(group);
            }
        }
    }

    private void recruitPlayer(String player) {
        int moneyNeeded = getRecruitCost(player);
        recruitingBudget -= moneyNeeded;
        budgetText.setText("Budget: $" + recruitingBudget);

        // Remove the player from the top 100 list
        if (avail50.contains(player)) {
            avail50.remove(player);
        }
        if (availAll.contains(player)) {
            availAll.remove(player);
        }
        if (west.contains(player)) {
            west.remove(player);
        }
        if (midwest.contains(player)) {
            midwest.remove(player);
        }
        if (central.contains(player)) {
            central.remove(player);
        }
        if (east.contains(player)) {
            east.remove(player);
        }
        playersRecruited.add(player);

        // Also need to add recruited player to correct team list and remove from avail list
        String[] ps = player.split(",");
        if (ps[0].equals("QB")) {
            availQBs.remove(player);
            teamQBs.add(getReadablePlayerInfo(player));
            Collections.sort(teamQBs, new PlayerTeamStrCompOverall());
        } else if (ps[0].equals("RB")) {
            availRBs.remove(player);
            teamRBs.add(getReadablePlayerInfo(player));
            Collections.sort(teamRBs, new PlayerTeamStrCompOverall());
        } else if (ps[0].equals("WR")) {
            availWRs.remove(player);
            teamWRs.add(getReadablePlayerInfo(player));
            Collections.sort(teamWRs, new PlayerTeamStrCompOverall());
        } else if (ps[0].equals("TE")) {
            availTEs.remove(player);
            teamTEs.add(getReadablePlayerInfo(player));
            Collections.sort(teamTEs, new PlayerTeamStrCompOverall());
        } else if (ps[0].equals("OL")) {
            availOLs.remove(player);
            teamOLs.add(getReadablePlayerInfo(player));
            Collections.sort(teamOLs, new PlayerTeamStrCompOverall());
        } else if (ps[0].equals("K")) {
            availKs.remove(player);
            teamKs.add(getReadablePlayerInfo(player));
            Collections.sort(teamKs, new PlayerTeamStrCompOverall());
        } else if (ps[0].equals("DL")) {
            availDLs.remove(player);
            teamDLs.add(getReadablePlayerInfo(player));
            Collections.sort(teamDLs, new PlayerTeamStrCompOverall());
        } else if (ps[0].equals("LB")) {
            availLBs.remove(player);
            teamLBs.add(getReadablePlayerInfo(player));
            Collections.sort(teamLBs, new PlayerTeamStrCompOverall());
        } else if (ps[0].equals("CB")) {
            availCBs.remove(player);
            teamCBs.add(getReadablePlayerInfo(player));
            Collections.sort(teamCBs, new PlayerTeamStrCompOverall());
        } else if (ps[0].equals("S")) {
            availSs.remove(player);
            teamSs.add(getReadablePlayerInfo(player));
            Collections.sort(teamSs, new PlayerTeamStrCompOverall());
        }

        players.remove(player);

        Toast.makeText(this, "Recruited " + ps[0] + " " + ps[1],
                Toast.LENGTH_SHORT).show();

        updatePositionNeeds();
    }

    /**
     * Recruit player, add to correct list and remove from available players list
     */
    private void redshirtPlayerDialog(String p, int pos, List<Integer> groupsExp) {
        final String player = p;
        final int groupPosition = pos;
        final List<Integer> groupsExpanded = groupsExp;
        int moneyNeeded = getRecruitCost(player);
        if (recruitingBudget >= moneyNeeded) {

            if (showPopUp) {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Confirm Redshirting");
                builder.setMessage("Your team roster is at " + (teamPlayers.size()+playersRecruited.size()+playersRedshirted.size()) + " (Max: 70).\n" +
                        "You currently have redshirted " + redshirtCount + " players (Max: " + maxRedshirt + ").\n\n" +
                        "Are you sure you want to redshirt " + player.split(",")[0] + " " + getReadablePlayerInfoDisplay(player) + " for $" + moneyNeeded + "?\n" +
                        "He will be unavailable to play for the first year.");
                builder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                recruitList.collapseGroup(groupPosition);
                                for (int i = groupPosition + 1; i < players.size(); ++i) {
                                    if (recruitList.isGroupExpanded(i)) {
                                        groupsExpanded.add(i);
                                    }
                                    recruitList.collapseGroup(i);
                                }

                                redshirtPlayer(player);

                                expListAdapter.notifyDataSetChanged();
                                for (int group : groupsExpanded) {
                                    recruitList.expandGroup(group - 1);
                                }
                                redshirtCount++;
                                dialog.dismiss();
                            }
                        });

                builder.setNeutralButton("Yes, Don't Show",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                recruitList.collapseGroup(groupPosition);
                                for (int i = groupPosition + 1; i < players.size(); ++i) {
                                    if (recruitList.isGroupExpanded(i)) {
                                        groupsExpanded.add(i);
                                    }
                                    recruitList.collapseGroup(i);
                                }

                                redshirtPlayer(player);
                                setShowPopUp(false);

                                expListAdapter.notifyDataSetChanged();
                                for (int group : groupsExpanded) {
                                    recruitList.expandGroup(group - 1);
                                }
                                dialog.dismiss();
                            }
                        });

                builder.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // not successful
                                recruitList.collapseGroup(groupPosition);
                                for (int i = groupPosition + 1; i < players.size(); ++i) {
                                    if (recruitList.isGroupExpanded(i)) {
                                        groupsExpanded.add(i);
                                    }
                                    recruitList.collapseGroup(i);
                                }

                                recruitList.expandGroup(groupPosition);
                                expListAdapter.notifyDataSetChanged();
                                for (int group : groupsExpanded) {
                                    recruitList.expandGroup(group);
                                }
                                dialog.cancel();
                            }
                        });

                AlertDialog dialog = builder.create();
                dialog.show();

                TextView msgTxt = dialog.findViewById(android.R.id.message);
                msgTxt.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);

            } else {
                // Don't show pop up dialog
                recruitList.collapseGroup(groupPosition);
                for (int i = groupPosition + 1; i < players.size(); ++i) {
                    if (recruitList.isGroupExpanded(i)) {
                        groupsExpanded.add(i);
                    }
                    recruitList.collapseGroup(i);
                }

                redshirtPlayer(player);

                expListAdapter.notifyDataSetChanged();
                for (int group : groupsExpanded) {
                    recruitList.expandGroup(group - 1);
                }
            }

        } else {
            recruitList.collapseGroup(groupPosition);
            for (int i = groupPosition + 1; i < players.size(); ++i) {
                if (recruitList.isGroupExpanded(i)) {
                    groupsExpanded.add(i);
                }
                recruitList.collapseGroup(i);
            }
            Toast.makeText(this, "Not enough money!",
                    Toast.LENGTH_SHORT).show();
            recruitList.expandGroup(groupPosition);
            expListAdapter.notifyDataSetChanged();
            for (int group : groupsExpanded) {
                recruitList.expandGroup(group);
            }
        }
    }

    private void redshirtPlayer(String player) {
        int moneyNeeded = getRecruitCost(player);
        recruitingBudget -= moneyNeeded;
        budgetText.setText("Budget: $" + recruitingBudget);

        // Remove the player from the top 100 list
        if (avail50.contains(player)) {
            avail50.remove(player);
        }
        if (availAll.contains(player)) {
            availAll.remove(player);
        }
        if (west.contains(player)) {
            west.remove(player);
        }
        if (midwest.contains(player)) {
            midwest.remove(player);
        }
        if (central.contains(player)) {
            central.remove(player);
        }
        if (east.contains(player)) {
            east.remove(player);
        }
        playersRedshirted.add(player);

        // Also need to add recruited player to correct team list and remove from avail list
        String[] ps = player.split(",");
        if (ps[0].equals("QB")) {
            availQBs.remove(player);
        } else if (ps[0].equals("RB")) {
            availRBs.remove(player);
        } else if (ps[0].equals("WR")) {
            availWRs.remove(player);
        } else if (ps[0].equals("TE")) {
            availTEs.remove(player);
        } else if (ps[0].equals("OL")) {
            availOLs.remove(player);
        } else if (ps[0].equals("K")) {
            availKs.remove(player);
        } else if (ps[0].equals("DL")) {
            availDLs.remove(player);
        } else if (ps[0].equals("LB")) {
            availLBs.remove(player);
        } else if (ps[0].equals("CB")) {
            availCBs.remove(player);
        } else if (ps[0].equals("S")) {
            availSs.remove(player);
        }

        players.remove(player);

        Toast.makeText(this, "Redshirted " + ps[0] + " " + ps[1],
                Toast.LENGTH_SHORT).show();

        updatePositionNeeds();
    }

    /**
     * Scout player, revealing the attribute ratings.
     *
     * @param player
     * @return true if had enough money, false if not
     */
    private boolean scoutPlayer(String player) {
        int scoutCost = getRecruitCost(player) / 10;
        if (scoutCost < 10) scoutCost = 10;

        if (recruitingBudget >= scoutCost) {
            recruitingBudget -= scoutCost;
            budgetText.setText("Budget: $" + recruitingBudget);

            // Check availAll first
            if (availAll.contains(player)) {
                int posTop = availAll.indexOf(player);
                availAll.set(posTop, player.substring(0, player.length() - 1) + "1");
            }
            if (avail50.contains(player)) {
                int posTop = avail50.indexOf(player);
                avail50.set(posTop, player.substring(0, player.length() - 1) + "1");
            }


            // Next check all the position lists
            String[] ps = player.split(",");
            if (ps[0].equals("QB") && availQBs.contains(player)) {
                availQBs.set(availQBs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("RB") && availRBs.contains(player)) {
                availRBs.set(availRBs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("WR") && availWRs.contains(player)) {
                availWRs.set(availWRs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("TE") && availTEs.contains(player)) {
                availTEs.set(availTEs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("OL") && availOLs.contains(player)) {
                availOLs.set(availOLs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("K") && availKs.contains(player)) {
                availKs.set(availKs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("DL") && availDLs.contains(player)) {
                availDLs.set(availDLs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("LB") && availLBs.contains(player)) {
                availLBs.set(availLBs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("CB") && availCBs.contains(player)) {
                availCBs.set(availCBs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            } else if (ps[0].equals("S") && availSs.contains(player)) {
                availSs.set(availSs.indexOf(player), player.substring(0, player.length() - 1) + "1");
            }

            Toast.makeText(this, "Scouted " + ps[0] + " " + ps[1], Toast.LENGTH_SHORT).show();

            expListAdapter.notifyDataSetChanged();

            return true;

        } else {
            Toast.makeText(this, "Not enough money!",
                    Toast.LENGTH_SHORT).show();

            return false;
        }
    }

    /**
     * Inner Class used for the recruiting expandable list view
     */
    public class ExpandableListAdapterRecruiting extends BaseExpandableListAdapter {

        private Activity context;

        public ExpandableListAdapterRecruiting(Activity context) {
            this.context = context;
        }

        public String getChild(int groupPosition, int childPosition) {
            return playersInfo.get(players.get(groupPosition).substring(0, players.get(groupPosition).length() - 2)).get(childPosition);
        }

        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }


        public View getChildView(final int groupPosition, final int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {
            final String playerDetail = getChild(groupPosition, childPosition);
            final String playerCSV = getGroup(groupPosition);
            LayoutInflater inflater = context.getLayoutInflater();

            if (convertView == null) {
                convertView = inflater.inflate(R.layout.child_recruit, null);
            }

            // Set up Text for player details
            final TextView details = convertView.findViewById(R.id.textRecruitDetails);
            final TextView potential = convertView.findViewById(R.id.textRecruitPotential);

            details.setText(playerDetail);
            potential.setText("Height: " + getHeight(Integer.parseInt(playerCSV.split(",")[17])) + "\nWeight: " + getWeight(Integer.parseInt(playerCSV.split(",")[18])) + "\nFootball IQ: " + getGrade(playerCSV.split(",")[5]) + "\n" +
                    "Personality: " + getGrade(playerCSV.split(",")[4])+ "\n" +
                    "Durability: " + getGrade(playerCSV.split(",")[10]));

            // Set up Recruit and Redshirt buttons to display the right price
            Button recruitPlayerButton = convertView.findViewById(R.id.buttonRecruitPlayer);

            if(teamPlayers.size()+playersRecruited.size()+playersRedshirted.size() < maxPlayers) {
                recruitPlayerButton.setText("Recruit: $" + getRecruitCost(playerCSV));
            } else recruitPlayerButton.setVisibility(View.INVISIBLE);

            Button redshirtPlayerButton = convertView.findViewById(R.id.buttonRedshirtPlayer);
            if (redshirtCount < maxRedshirt) {
                redshirtPlayerButton.setText("Redshirt: $" + getRecruitCost(playerCSV));
            } else redshirtPlayerButton.setVisibility(View.INVISIBLE);

            // Set up button for recruiting player
            recruitPlayerButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Save who is currently expanded
                    if(teamPlayers.size()+playersRecruited.size()+playersRedshirted.size() < maxPlayers) {
                        List<Integer> groupsExpanded = new ArrayList<>();
                        recruitPlayerDialog(playerCSV, groupPosition, groupsExpanded);
                    }
                }
            });

            // Set up button for redshirting player
            redshirtPlayerButton.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Save who is currently expanded
                    if (redshirtCount < maxRedshirt && (teamPlayers.size()+playersRecruited.size()+playersRedshirted.size()) < maxPlayers) {
                        List<Integer> groupsExpanded = new ArrayList<>();
                        redshirtPlayerDialog(playerCSV, groupPosition, groupsExpanded);
                    }
                }
            });

            return convertView;
        }

        public int getChildrenCount(int groupPosition) {
            return playersInfo.get(players.get(groupPosition).substring(0, players.get(groupPosition).length() - 2)).size();
        }

        public String getGroup(int groupPosition) {
            return players.get(groupPosition);
        }

        public int getGroupCount() {
            return players.size();
        }

        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {
            String[] playerCost = getPlayerNameCost(getGroup(groupPosition)).split(">");
            String playerLeft = playerCost[0];
            String playerRight = playerCost[1];
            if (convertView == null) {
                LayoutInflater infalInflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = infalInflater.inflate(R.layout.group_recruit,
                        null);
            }
            TextView itemL = convertView.findViewById(R.id.textRecruitLeft);
            itemL.setTypeface(null, Typeface.BOLD);
            itemL.setText(playerLeft);
            TextView itemR = convertView.findViewById(R.id.textRecruitRight);
            itemR.setTypeface(null, Typeface.BOLD);
            itemR.setText(playerRight);
            return convertView;
        }

        public boolean hasStableIds() {
            return true;
        }

        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }

    } //end class

    public String getRegion(int region) {
        String location;
        if (region == 0) location = "West";
        else if (region == 1) location = "Mid-West";
        else if (region == 2) location = "Central";
        else if (region == 3) location = "East";
        else location = "South";
        return location;
    }

    public String getHeight(int height) {

        int feet = height / 12;
        int leftover = height % 12;

        return feet + "'' " + leftover + "\"";
    }

    public String getWeight(int weight) {
        return weight + " lbs";
    }


}

class PlayerRecruitStrCompOverall implements Comparator<String> {
    @Override
    public int compare(String a, String b) {
        String[] psA = a.split(",");
        String[] psB = b.split(",");
        float ovrA = (4*Integer.parseInt(psA[11])+Integer.parseInt(psA[9])) / 5;
        float ovrB = (4*Integer.parseInt(psB[11])+Integer.parseInt(psB[9])) / 5;
        return ovrA > ovrB ? -1 : ovrA == ovrB ? 0 : 1;
    }
}

class PlayerTeamStrCompOverall implements Comparator<String> {
    @Override
    public int compare(String a, String b) {
        String[] psA = a.split(" ");
        String[] psB = b.split(" ");
        int ovrA = Integer.parseInt(psA[4]);
        int ovrB = Integer.parseInt(psB[4]);
        return ovrA > ovrB ? -1 : ovrA == ovrB ? 0 : 1;
    }
}

