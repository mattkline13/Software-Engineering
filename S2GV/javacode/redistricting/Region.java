package redistricting;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Region is represented as a rectangular grid where each cell is a voter.
 * Each cell is colored based on the party of that voter.
 * Region can be divided into districts.
 * Dividing the region is handled by Districting.java.
 * Regions have yet to implement the serializable class.
 * @author S2GV Josh McKinstry, Matt Kline, Dong (Bob) Lee
 */
public class Region implements Serializable {

    /** Region represented as a grid of voters. */
    private Voter[][] regionGrid;
    /** String identifier to distinguish regions. */
    private String regionID;
    /** Truth value identifies whether region has been districted. */
    private boolean isDistricted;
    /** List of the districts that divide the region. */
    private List<District> regionDistricts;
    /** Party represented by the majority of districts is favored. */
    private char regionFavoredParty;
    /** Stores the amount of voters in the region. */
    private int counterForVoters;

    /**
     * Constructor builds new undistricted region with no dimensions.
     * @param regionID String to identify the region
     */
    public Region(final String regionID) {
        this.regionID = regionID;
        this.isDistricted = false;
        regionGrid = new Voter[0][0];
        regionDistricts = new ArrayList<District>();
        counterForVoters = 0;
    }

    /**
     * Constructor builds new undistricted region with dimensions.
     * @param regionID String to identify the region
     * @param rows Vertical dimension of regionGrid
     * @param columns Horizontal dimension of regionGrid
     */
    public Region(final String regionID, final int rows, final int columns) {
        this.regionID = regionID;
        this.isDistricted = false;
        regionGrid = new Voter[rows][columns];
        regionDistricts = new ArrayList<District>();
        counterForVoters = 0;
    }

    /**
     * method will return the regions districts.
     * @return the list of districts
     */
    public List<District> getRegionDistricts() {
        return regionDistricts;
    }

    /**
     * Add a voter to the region, to be used by populateRegion method.
     * @param voter Voter to be added
     */
    public void addVoter(final Voter voter) {
        if (counterForVoters == 0) {
            regionGrid[0][0] = voter;
            counterForVoters++;
            return;
        } else if (regionGrid[0].length < counterForVoters) {
            regionGrid[0][counterForVoters] = voter;
            counterForVoters++;
            return;
        } else {
            for (int i = 0;  i < regionGrid.length; i++) {
                int rowNumber = regionGrid[i].length * i;
                if (regionGrid[i].length < (counterForVoters - rowNumber)) {
                    regionGrid[i][counterForVoters - rowNumber] = voter;
                    counterForVoters++;
                    return;
                }
            }
        }
    }

    /** Remove a voter from the region.
     * @param voter Voter to be removed
     */
    private void removeVoter(final Voter voter) {
        if (regionGrid.length == 0) {
            return;
        }
        --counterForVoters;
        for (int index = 0; index < regionGrid.length; index++) {
            for (int inner = 0; inner < regionGrid[index].length; inner++) {
                if (regionGrid[index][inner] == voter) {
                    regionGrid[index][inner] = null;
                    return;
                 }
             }
         }
    }
    /**
     * Retrieve unique region identifier.
     * @return regionID Unique String to distinguish regions
     */
    public String getID() {
        return this.regionID;
    }

    /**
     * Set a unique region identifier.
     * @param regionID Unique String to distinguish regions
     */
    public void setID(final String regionID) {
        this.regionID = regionID;
    }

    /**
     * Gives region dimensions, then populates empty
     * region spaces with undeclared voters.
     * @param rows Desired vertical dimension of region
     * @param columns Desired horizontal dimension of region
     */
    public void populateRegion(final int rows, final int columns) {
        Voter[][] regionGrid = new Voter[rows][columns];
        for (int row = 0; row < regionGrid.length; row++) {
            for (int column = 0; column < regionGrid[row].length; column++) {
                if (regionGrid[row][column] == null) {
                    String voterID = row + "" + column;
                    Voter noPartyVoter = new Voter(Integer.parseInt(voterID));
                    regionGrid[row][column] = noPartyVoter;
                }
            }
        }
    }

    /**
     * An already sized region populates all empty region spaces
     * with undeclared voters.
     */
    public void populateRegion() {
        for (int row = 0; row < regionGrid.length; row++) {
            for (int column = 0; column < regionGrid[row].length; column++) {
                if (regionGrid[row][column] == null) {
                    String voterID = row + "" + column;
                    Voter noPartyVoter = new Voter(Integer.parseInt(voterID));
                    regionGrid[row][column] = noPartyVoter;
                }
            }
        }
    }

    /**
     * User manually declared party affiliation for each voter.
     * @param parties takes the party input for assigning
     */
    public void setPartyManual(final String parties) {
        int i = 0;
        for (int row = 0; row < regionGrid.length; row++) {
            for (int column = 0; column < regionGrid[row].length; column++) {
                regionGrid[row][column].setParty(parties.charAt(i));
                i++;
            }
        }
    }

    /**
     * Randomly declares party for each voter in the region.
     */
    public void setPartyAuto() {
        Random rand = new Random();

        int  n;
        for (int row = 0; row < regionGrid.length; row++) {
            for (int column = 0; column < regionGrid[row].length; column++) {
                n = rand.nextInt(2);
                if (n == 0) {
                    regionGrid[row][column].setParty('A');
                } else {
                    regionGrid[row][column].setParty('B');
                }
            }
        }
    }

    /**
     * Change party of single voter in grid by their voterID.
     * @param voterID ID of voter of which to change party
     */
    public void changePartyByID(final int voterID) {
        for (int index = 0; index < regionGrid.length; index++) {
            for (int inner = 0; inner < regionGrid[index].length; inner++) {
                if (regionGrid[index][inner].getID() == voterID) {
                   if (regionGrid[index][inner].getParty() == 'a') {
                        regionGrid[index][inner].setParty('b');
                    } else {
                        regionGrid[index][inner].setParty('a');
                    }
                }
            }
        }
    }

    /**
     * Change party of single voter by their location in the region.
     * @param row The vertical location of voter in grid
     * @param column The horizontal location of voter in grid
     */
    public void changePartyByCell(final int row, final int column) {
        if (regionGrid[row][column].getParty() == 'a') {
            regionGrid[row][column].setParty('b');
        } else {
            regionGrid[row][column].setParty('a');
        }
    }

    /**
     * Draw a graphical representation of the region using color.
     * Has yet to be implemented.
     */
    public void drawRegionGrid() {
        for (int row = 0; row < regionGrid.length; row++) {
            for (int column = 0; column < regionGrid[row].length; column++) {
                System.out.print("[" +
                        regionGrid[row][column].getParty() + "]");
            }
            System.out.print("\n");
        }
    }

    /**
     * Clear the region of voters, retains size.
     */
    public void clearRegion() {
        for (int i = 0; i < regionGrid.length; i++) {
            regionGrid[i] = null;
         }
        counterForVoters = 0;
    }

    /**
     * Overrides the size of the region.
     * @param rows New desired vertical dimension
     * @param columns New desired horizontal dimension
     */
    public void resetSize(final int rows, final int columns) {
        regionGrid = new Voter[rows][columns];
    }

    /**
     * Retrieve the Favored Party of the region.
     * @return regionFavoredParty
     */
    public char getFavoredParty() {
        return this.regionFavoredParty;
    }

    /**
     * Sets Favored Party of the region.
     * @param favoredParty Majority party in region
     */
    public void setFavoredParty(final char favoredParty) {
        this.regionFavoredParty = favoredParty;
    }

    /**
     * Returns the size of the region.
     * @return returns size of the region
     */
     public int getRegionSize() {
        int regionSize = regionGrid[0].length * regionGrid.length;
        return regionSize;
    }

    /**
     * Retrieves a certain voter in a region by the cell.
     * @param row Row in the grid where voter is located
     * @param column Column in the grid where voter is located
     * @return Voter in cell
     */
    public Voter getVoterByCell(final int row, final int column) {
        return regionGrid[row][column];
    }
}