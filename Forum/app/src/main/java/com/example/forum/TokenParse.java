package com.example.forum;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TokenParse {
    private String location;
    private List<Integer> priceRange;
    private int bedrooms;

    public TokenParse(String input) {
        this.location = extractLocation(input);
        this.priceRange = extractMinMaxPrice(input);
        this.bedrooms = extractBedrooms(input);
    }

    public String getLocation() {
        return location;
    }

    public List<Integer> getpriceRange() {
        return priceRange;
    }

    public int getBedrooms() {
        return bedrooms;
    }

    private String extractLocation(String input) {

        String[] parts = input.split("\\s+");

        String FindLocation = null;
        for (String part : parts) {
            // Use regular expressions to check for the presence of letters, if found, identify it as a location
            if (containsLetters(part)) {
                if (part.matches("(?i)^cit.*$")) {
                    FindLocation = "City";
                } else if (part.matches("(?i)^kin.*$")) {
                    FindLocation = "Kingston";
                } else if (part.matches("(?i)^bra.*$")) {
                    FindLocation = "Braddon";
                } else if (part.matches("(?i)^bar.*$")) {
                    FindLocation = "Barton";
                } else if (part.matches("(?i)^dea.*$")) {
                    FindLocation = "Deakin";
                } else if (part.matches("(?i)^yar.*$")) {
                    FindLocation = "Yarralumla";
                } else if (part.matches("(?i)^rei.*$")) {
                    FindLocation = "Reid";
                } else if (part.matches("(?i)^act.*$")) {
                    FindLocation = "Acton";
                } else if (part.matches("(?i)^cent.*$")) {
                    FindLocation = "Central";
                } else if (part.matches("(?i)^bon.*$")) {
                    FindLocation = "Bondi";
                } else if (part.matches("(?i)^darl.*$")) {
                    FindLocation = "Darlinghurst";
                } else if (part.matches("(?i)^pad.*$")) {
                    FindLocation = "Paddington";
                } else if (part.matches("(?i)^chip.*$")) {
                    FindLocation = "Chippendale";
                } else if (part.matches("(?i)^py.*$")) {
                    FindLocation = "Pyrmont";
                } else if (part.matches("(?i)^sur.*$")) {
                    FindLocation = "Surry Hills";
                } else if (part.matches("(?i)^red.*$")) {
                    FindLocation = "Redfern";
                } else if (part.matches("(?i)^ric.*$")) {
                    FindLocation = "Richmond";
                } else if (part.matches("(?i)^sou.*$")) {
                    FindLocation = "Southbank";
                } else if (part.matches("(?i)^car.*$")) {
                    FindLocation = "Carlton";
                } else if (part.matches("(?i)^fit.*$")) {
                    FindLocation = "Fitzroy";
                } else if (part.matches("(?i)^st.*$")) {
                    FindLocation = "St Kilda";
                } else if (part.matches("(?i)^doc.*$")) {
                    FindLocation = "Docklands";
                } else if (part.matches("(?i)^fort.*$")) {
                    FindLocation = "Fortitude Valley";
                } else if (part.matches("(?i)^nor.*$")) {
                    FindLocation = "North Adelaide";
                } else if (part.matches("(?i)^gle.*$")) {
                    FindLocation = "Glenelg";
                } else if (part.matches("(?i)^norw.*$")) {
                    FindLocation = "Norwood";
                } else if (part.matches("(?i)^por.*$")) {
                    FindLocation = "Port Adelaide";
                } else if (part.matches("(?i)^pro.*$")) {
                    FindLocation = "Prospect";
                } else if (part.matches("(?i)^un.*$")) {
                    FindLocation = "Unley";
                } else if (part.matches("(?i)^fre.*$")) {
                    FindLocation = "Fremantle";
                } else if (part.matches("(?i)^sca.*$")) {
                    FindLocation = "Scarborough";
                } else if (part.matches("(?i)^sub.*$")) {
                    FindLocation = "Subiaco";
                } else if (part.matches("(?i)^cot.*$")) {
                    FindLocation = "Cottesloe";
                } else if (part.matches("(?i)^cla.*$")) {
                    FindLocation = "Claremont";
                } else if (part.matches("(?i)^joo.*$")) {
                    FindLocation = "Joondalup";
                } else if (part.matches("(?i)^dar.*$")) {
                    FindLocation = "Darwin City";
                } else if (part.matches("(?i)^par.*$")) {
                    FindLocation = "Parap";
                } else if (part.matches("(?i)^nig.*$")) {
                    FindLocation = "Nightcliff";
                } else if (part.matches("(?i)^fan.*$")) {
                    FindLocation = "Fannie Bay";
                } else if (part.matches("(?i)^lu.*$")) {
                    FindLocation = "Ludmilla";
                } else if (part.matches("(?i)^st.*$")) {
                    FindLocation = "Stuart Park";
                } else if (part.matches("(?i)^hob.*$")) {
                    FindLocation = "Hobart CBD";
                } else if (part.matches("(?i)^bat.*$")) {
                    FindLocation = "Battery Point";
                } else if (part.matches("(?i)^san.*$")) {
                    FindLocation = "Sandy Bay";
                } else if (part.matches("(?i)^north.*$")) {
                    FindLocation = "North Hobart";
                } else if (part.matches("(?i)^wes.*$")) {
                    FindLocation = "West Hobart";
                } else if (part.matches("(?i)^sou.*$")) {
                    FindLocation = "South Hobart";
                } else if (part.matches("(?i)^surf.*$")) {
                    FindLocation = "Surfers Paradise";
                } else if (part.matches("(?i)^bro.*$")) {
                    FindLocation = "Broadbeach";
                } else if (part.matches("(?i)^bur.*$")) {
                    FindLocation = "Burleigh Heads";
                } else if (part.matches("(?i)^coo.*$")) {
                    FindLocation = "Coolangatta";
                } else if (part.matches("(?i)^south.*$")) {
                    FindLocation = "Southport";
                } else if (part.matches("(?i)^rob.*$")) {
                    FindLocation = "Robina";
                }
            }
        }

        return FindLocation;
    }

    private boolean containsLetters(String input) {
        // Use regular expressions to check if the string contains letters
        Pattern pattern = Pattern.compile(".*[a-zA-Z].*");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }

    private List<Integer> extractMinMaxPrice(String input) {
        List<Integer> priceRange = new ArrayList<>();
        boolean validInput = false;
        //get price range
        String[] parts = input.split("\\s+");
            for (String part : parts) {
                if(!containsLetters(part.toString())){
                    if (part.contains("-")) {
                    validInput = true;
                    String[] rangeParts = part.split("-");
                    if (rangeParts.length == 2) {
                        int firstNumber = Integer.parseInt(rangeParts[0]);
                        int secondNumber = Integer.parseInt(rangeParts[1]);
                        priceRange.add(Math.min(firstNumber, secondNumber));
                        priceRange.add(Math.max(firstNumber, secondNumber));
                    }
                } else {
                    try {
                        int number = Integer.parseInt(part);
                        if (number > 100) {
                            priceRange.add(number - 100);
                            priceRange.add(number + 100);
                            validInput = true;
                        }
                    } catch (NumberFormatException e) {
                    }
                }
            }
        }


        if (validInput) {
            return priceRange;
        } else {
            return null;
        }
    }

    private int extractBedrooms(String input) {
        String[] parts = input.split("\\s+");
        //get bedroom numbers
        int FindroomNumber = 0;
        for (String part : parts) {
            if (part.matches("\\d+") && Integer.parseInt(part) < 7) {
                FindroomNumber = Integer.parseInt(part);
                break;
            }
        }
        return FindroomNumber;
    }

}



