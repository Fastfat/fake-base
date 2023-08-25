import exceptions.WrongColumnException;
import exceptions.WrongFormatException;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QuestMaker {
    private List<Map<String,Object>> data = new ArrayList<>();

    public QuestMaker() {

    }

    public List<Map<String,Object>> getList(String request) {
        String[] a = request.split(" ");
        String command = a[0];
        String[] requestMass2;
        Pattern pattern = Pattern.compile("where", Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(request);
        request = matcher.replaceFirst("where");

        switch (command.toUpperCase()) {
            case "INSERT":
                requestMass2 = request.split(",");
                for (int i = 0; i < requestMass2.length; i++) {
                    requestMass2[i].trim();
                }
                return doInsert(requestMass2);
            case "UPDATE":
                requestMass2 = request.split("where");
                doUpdate(requestMass2);
                return data;
            case "DELETE":
                requestMass2 = request.split("where");
                return doDelete(requestMass2);
            case "SELECT":
                requestMass2 = request.split("where");
                return doSelect(requestMass2);
            default:
                return null;

        }
    }

    private List<Map<String, Object>> doInsert(String[] commandMass) {
        Map<String,Object> row;
        String[] info2 = null;
        row = cleanMap();
        for (int i = 0; i < commandMass.length; i++) {
            info2 = commandMass[i].split("=");
            if (info2[0].toLowerCase().contains("'lastname'")) {
                info2[1] = info2[1].trim();
                if (!info2[1].equals("null")) {
                    row.put("lastName", info2[1].substring(1, info2[1].length() - 1));
                } else {
                    row.put("lastName", null);
                }
            } else if (info2[0].toLowerCase().contains("'id'")) {
                info2[1] = info2[1].trim();
                if (!info2[1].equals("null")) {
                    row.put("id", Long.parseLong(info2[1]));
                } else {
                    row.put("id", null);
                }
            } else if (info2[0].toLowerCase().contains("'cost'")) {
                info2[1] = info2[1].trim();
                if (!info2[1].equals("null")) {
                    row.put("cost", Double.parseDouble(info2[1]));
                } else {
                    row.put("cost", null);
                }
            } else if (info2[0].toLowerCase().contains("'age'")) {
                info2[1] = info2[1].trim();
                if (!info2[1].equals("null")) {
                    row.put("age", Long.parseLong(info2[1]));
                } else {
                    row.put("age", null);
                }
            } else if (info2[0].toLowerCase().contains("'active'")) {
                info2[1] = info2[1].trim();
                if (!info2[1].equals("null")) {
                    row.put("active", Boolean.parseBoolean(info2[1]));
                } else {
                    row.put("active", null);
                }
            } else {
                throw new WrongColumnException("Wrong column name!");
            }
        }
        if (info2 != null) {
            data.add(row);
        }
        return data;
    }

    private void doUpdate(String[] commandMass) {
        Map<String,Object> row;
        String[] updMass = commandMass[0].split(",");
        String[] info2 = null;
        String[] andMass = null;
        String[] orMass = null;
        ArrayList<Map<String,Object>> acceptList;
        ArrayList<Map<String,Object>> refuseList;

        row = cleanMap();
        for (int i = 0; i < updMass.length; i++) {
            info2 = updMass[i].split("=");
            if (info2[0].toLowerCase().contains("'lastname'")) {
                info2[1] = info2[1].trim();
                if (!info2[1].equals("null")) {
                    row.put("lastName", info2[1].substring(1, info2[1].length() - 1));
                } else {
                    row.put("lastName", "null");
                }
            } else if (info2[0].toLowerCase().contains("'id'")) {
                info2[1] = info2[1].trim();
                if (!info2[1].equals("null")) {
                    row.put("id", Long.parseLong(info2[1]));
                } else {
                    row.put("id", "null");
                }
            } else if (info2[0].toLowerCase().contains("'cost'")) {
                info2[1] = info2[1].trim();
                if (!info2[1].equals("null")) {
                    row.put("cost", Double.parseDouble(info2[1]));
                } else {
                    row.put("cost", "null");
                }
            } else if (info2[0].toLowerCase().contains("'age'")) {
                info2[1] = info2[1].trim();
                if (!info2[1].equals("null")) {
                    row.put("age", Long.parseLong(info2[1]));
                } else {
                    row.put("age", "null");
                }
            } else if (info2[0].toLowerCase().contains("'active'")) {
                info2[1] = info2[1].trim();
                if (!info2[1].equals("null")) {
                    row.put("active", Boolean.parseBoolean(info2[1]));
                } else {
                    row.put("active", "null");
                }
            } else {
                throw new WrongColumnException("Wrong column name!");
            }
        }
        if (commandMass.length > 1) {
            if (commandMass[1].contains(",")) {
                andMass = commandMass[1].split(",");
            } else if (commandMass[1].contains("and") || commandMass[1].contains("AND")) {
                String andStr = "";
                for (String i : commandMass[1].split(" ")) {
                    if (i.equals("and")) {
                        andStr = andStr + " " + i.toUpperCase();
                    } else {
                        andStr = andStr + " " + i;
                    }
                }
                commandMass[1] = andStr;
                andMass = commandMass[1].split("AND");
            } else if (commandMass[1].contains("or") || commandMass[1].contains("OR")) {
                String andStr = "";
                for (String i : commandMass[1].split(" ")) {
                    if (i.equals("or")) {
                        andStr = andStr + " " + i.toUpperCase();
                    } else {
                        andStr = andStr + " " + i;
                    }
                }
                commandMass[1] = andStr;
                orMass = commandMass[1].split("OR");
            } else {
                andMass = commandMass[1].split(",");
            }
            if (info2 != null) {
                ArrayList<ArrayList<Map<String, Object>>> acRefList;
                if (andMass != null) {
                    acRefList = getSortedList(andMass);
                    acceptList = acRefList.get(0);
                    refuseList = acRefList.get(1);
                    List<Map<String, Object>> differences = new ArrayList<>(acceptList);
                    differences.removeAll(refuseList);
                    Set<Map<String, Object>> set = new LinkedHashSet<>(differences);
                    List<Map<String, Object>> listWithoutDuplicates = new ArrayList<>(set);
                    for (Map<String, Object> updMap : listWithoutDuplicates) {
                        Map<String, Object> oldMap = data.get(data.indexOf(updMap));
                        for (String mapKey : row.keySet()) {
                            if (row.get(mapKey) != null && row.get(mapKey) != ("null")) {
                                updMap.put(mapKey, row.get(mapKey));
                            }
                            if (row.get(mapKey) == ("null")) {
                                updMap.put(mapKey, null);
                            }
                        }
                        data.set(data.indexOf(oldMap), updMap);
                    }
                } else if (orMass != null) {
                    acRefList = getSortedList(orMass);
                    acceptList = acRefList.get(0);
                    Set<Map<String, Object>> set = new LinkedHashSet<>(acceptList);
                    List<Map<String, Object>> listWithoutDuplicates = new ArrayList<>(set);
                    for (Map<String, Object> updMap : listWithoutDuplicates) {
                        Map<String, Object> oldMap = data.get(data.indexOf(updMap));
                        for (String mapKey : row.keySet()) {
                            if (row.get(mapKey) != null && row.get(mapKey) != ("null")) {
                                updMap.put(mapKey, row.get(mapKey));
                            }
                            if (row.get(mapKey) == ("null")) {
                                updMap.put(mapKey, null);
                            }
                        }
                        data.set(data.indexOf(oldMap), updMap);
                    }
                }
            }
        } else {
            for (Map<String, Object> updMap : data) {
                Map<String, Object> oldMap = updMap;
                for (String mapKey : row.keySet()) {
                    if (row.get(mapKey) != null) {
                        updMap.put(mapKey, row.get(mapKey));
                    }
                }
                data.set(data.indexOf(oldMap), updMap);
            }
        }
    }

    private List<Map<String, Object>> doDelete(String[] commandMass) {
        List<Map<String, Object>> deleteList = new ArrayList();
        String[] andMass = null;
        String[] orMass = null;
        ArrayList<Map<String,Object>> acceptList;
        ArrayList<Map<String,Object>> refuseList;
        if (commandMass.length > 1) {
            if (commandMass[1].contains(",")) {
                andMass = commandMass[1].split(",");
            } else if (commandMass[1].contains("and") || commandMass[1].contains("AND")) {
                String andStr = "";
                for (String i : commandMass[1].split(" ")) {
                    if (i.equals("and")) {
                        andStr = andStr + " " + i.toUpperCase();
                    } else {
                        andStr = andStr + " " + i;
                    }
                }
                commandMass[1] = andStr;
                andMass = commandMass[1].split("AND");
            } else if (commandMass[1].contains("or") || commandMass[1].contains("OR")) {
                String andStr = "";
                for (String i : commandMass[1].split(" ")) {
                    if (i.equals("or")) {
                        andStr = andStr + " " + i.toUpperCase();
                    } else {
                        andStr = andStr + " " + i;
                    }
                }
                commandMass[1] = andStr;
                orMass = commandMass[1].split("OR");
            } else {
                andMass = commandMass[1].split(",");
            }
                ArrayList<ArrayList<Map<String, Object>>> acRefList;
                if (andMass != null) {
                    acRefList = getSortedList(andMass);
                    acceptList = acRefList.get(0);
                    refuseList = acRefList.get(1);
                    List<Map<String, Object>> differences = new ArrayList<>(acceptList);
                    differences.removeAll(refuseList);
                    Set<Map<String, Object>> set = new LinkedHashSet<>(differences);
                    List<Map<String, Object>> listWithoutDuplicates = new ArrayList<>(set);
                    for (Map<String, Object> delMap : listWithoutDuplicates) {
                        deleteList.add(data.get(data.indexOf(delMap)));
                        data.remove(data.indexOf(delMap));
                    }
                } else if (orMass != null) {
                    acRefList = getSortedList(orMass);
                    acceptList = acRefList.get(0);
                    Set<Map<String, Object>> set = new LinkedHashSet<>(acceptList);
                    List<Map<String, Object>> listWithoutDuplicates = new ArrayList<>(set);
                    for (Map<String, Object> delMap : listWithoutDuplicates) {
                        deleteList.add(data.get(data.indexOf(delMap)));
                        data.remove(data.indexOf(delMap));
                    }
                }
            return deleteList;
        } else {
            deleteList = new ArrayList<>(data);
            data.clear();
            return deleteList;
        }
    }

    private List<Map<String, Object>> doSelect(String[] commandMass) {
        List<Map<String, Object>> selectList = new ArrayList();
        String[] andMass = null;
        String[] orMass = null;
        ArrayList<Map<String,Object>> acceptList;
        ArrayList<Map<String,Object>> refuseList;
        if (commandMass.length > 1) {
            if (commandMass[1].contains(",")) {
                andMass = commandMass[1].split(",");
            } else if (commandMass[1].contains("and") || commandMass[1].contains("AND")) {
                String andStr = "";
                for (String i : commandMass[1].split(" ")) {
                    if (i.equals("and")) {
                        andStr = andStr + " " + i.toUpperCase();
                    } else {
                        andStr = andStr + " " + i;
                    }
                }
                commandMass[1] = andStr;
                andMass = commandMass[1].split("AND");
            } else if (commandMass[1].contains("or") || commandMass[1].contains("OR")) {
                String andStr = "";
                for (String i : commandMass[1].split(" ")) {
                    if (i.equals("or")) {
                        andStr = andStr + " " + i.toUpperCase();
                    } else {
                        andStr = andStr + " " + i;
                    }
                }
                commandMass[1] = andStr;
                orMass = commandMass[1].split("OR");
            } else {
                andMass = commandMass[1].split(",");
            }
            ArrayList<ArrayList<Map<String, Object>>> acRefList;
            if (andMass != null) {
                acRefList = getSortedList(andMass);
                acceptList = acRefList.get(0);
                refuseList = acRefList.get(1);
                List<Map<String, Object>> differences = new ArrayList<>(acceptList);
                differences.removeAll(refuseList);
                Set<Map<String, Object>> set = new LinkedHashSet<>(differences);
                List<Map<String, Object>> listWithoutDuplicates = new ArrayList<>(set);
                for (Map<String, Object> selMap : listWithoutDuplicates) {
                    selectList.add(data.get(data.indexOf(selMap)));
                }
            } else if (orMass != null) {
                acRefList = getSortedList(orMass);
                acceptList = acRefList.get(0);
                Set<Map<String, Object>> set = new LinkedHashSet<>(acceptList);
                List<Map<String, Object>> listWithoutDuplicates = new ArrayList<>(set);
                for (Map<String, Object> selMap : listWithoutDuplicates) {
                    selectList.add(data.get(data.indexOf(selMap)));
                }
            }
            return selectList;
        } else {
            return data;
        }
    }

    private ArrayList<ArrayList<Map<String, Object>>> getSortedList(String[] andMass) {
        ArrayList<ArrayList<Map<String, Object>>> acRefList = new ArrayList<>();
        ArrayList<Map<String, Object>> acceptList = new ArrayList<>();
        ArrayList<Map<String, Object>> refuseList = new ArrayList<>();
        String[] info2;
        for (String str : andMass) {
            if (str.contains(">=")) {
                info2 = str.split(">=");
                if (info2[0].trim().toLowerCase().equals("'id'")) {
                    Long id = Long.parseLong(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if ((Long)(i.get("id")) >= id) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (info2[0].trim().toLowerCase().equals("'age'")) {
                    Long age = Long.parseLong(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if ((Long) (i.get("age")) >= age) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (info2[0].trim().toLowerCase().equals("'cost'")) {
                    Double cost = Double.parseDouble(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if ((Double) (i.get("cost")) >= cost) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (!info2[0].trim().toLowerCase().equals("'lastname'") || !info2[0].trim().toLowerCase().equals("'active'")) {
                    throw new WrongColumnException("Wrong column name!");
                } else {
                    throw new WrongFormatException("Wrong format for the column!");
                }
            } else if (str.contains("<=")) {
                info2 = str.split("<=");
                if (info2[0].trim().toLowerCase().equals("'id'")) {
                    Long id = Long.parseLong(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if ((Long)(i.get("id")) <= id) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (info2[0].trim().toLowerCase().equals("'age'")) {
                    Long age = Long.parseLong(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if ((Long) (i.get("age")) <= age) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (info2[0].trim().toLowerCase().equals("'cost'")) {
                    Double cost = Double.parseDouble(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if ((Double) (i.get("cost")) <= cost) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (!info2[0].trim().toLowerCase().equals("'lastname'") || !info2[0].trim().toLowerCase().equals("'active'")) {
                    throw new WrongColumnException("Wrong column name!");
                } else {
                    throw new WrongFormatException("Wrong format for the column!");
                }
            } else if (str.contains("!=")) {
                info2 = str.split("!=");
                if (info2[0].trim().toLowerCase().equals("'id'")) {
                    Long id = Long.parseLong(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if (id != (Long)(i.get("id"))) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (info2[0].trim().toLowerCase().equals("'age'")) {
                    Long age = Long.parseLong(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if (age != (Long) (i.get("age"))) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (info2[0].trim().toLowerCase().equals("'cost'")) {
                    Double cost = Double.parseDouble(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if (cost != (Double) (i.get("cost"))) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (!info2[0].trim().toLowerCase().equals("'lastname'") || !info2[0].trim().toLowerCase().equals("'active'")) {
                    throw new WrongColumnException("Wrong column name!");
                } else {
                    throw new WrongFormatException("Wrong format for the column!");
                }
            } else if (str.contains(">")) {
                info2 = str.split(">");
                if (info2[0].trim().toLowerCase().equals("'id'")) {
                    Long id = Long.parseLong(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if ((Long) (i.get("id")) > id) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (info2[0].trim().toLowerCase().equals("'age'")) {
                    Long age = Long.parseLong(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if ((Long) (i.get("age")) > age) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (info2[0].trim().toLowerCase().equals("'cost'")) {
                    Double cost = Double.parseDouble(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if ((Double) (i.get("cost")) > cost) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (!info2[0].trim().toLowerCase().equals("'lastname'") || !info2[0].trim().toLowerCase().equals("'active'")) {
                    throw new WrongColumnException("Wrong column name!");
                } else {
                    throw new WrongFormatException("Wrong format for the column!");
                }
            } else if (str.contains("<")) {
                info2 = str.split("<");
                if (info2[0].trim().toLowerCase().equals("'id'")) {
                    Long id = Long.parseLong(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if ((Long)(i.get("id")) < id) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (info2[0].trim().toLowerCase().equals("'age'")) {
                    Long age = Long.parseLong(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if ((Long) (i.get("age")) < age) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (info2[0].trim().toLowerCase().equals("'cost'")) {
                    Double cost = Double.parseDouble(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if ((Double) (i.get("cost")) < cost) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (!info2[0].trim().toLowerCase().equals("'lastname'") || !info2[0].trim().toLowerCase().equals("'active'")) {
                    throw new WrongColumnException("Wrong column name!");
                } else {
                    throw new WrongFormatException("Wrong format for the column!");
                }
            } else if (str.contains("=")) {
                info2 = str.split("=");
                if (info2[0].trim().toLowerCase().equals("'id'")) {
                    Long id = Long.parseLong(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if (id == (Long)(i.get("id"))) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (info2[0].trim().toLowerCase().equals("'age'")) {
                    Long age = Long.parseLong(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if (age == (Long) (i.get("age"))) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (info2[0].trim().toLowerCase().equals("'active'")) {
                    Boolean active = Boolean.parseBoolean(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if (active == (Boolean) (i.get("active"))) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (info2[0].trim().toLowerCase().equals("'cost'")) {
                    Double cost = Double.parseDouble(info2[1].trim());
                    for (Map<String, Object> i : data) {
                        if (cost == (Double) (i.get("cost"))) {
                            acceptList.add(i);
                        } else {
                            refuseList.add(i);
                        }
                    }
                } else if (!info2[0].trim().toLowerCase().equals("'lastname'")) {
                    throw new WrongColumnException("Wrong column name!");
                } else {
                    throw new WrongFormatException("Wrong format for the column!");
                }
            } else if (str.contains("ilike")) {
                info2 = str.split("ilike");
                if (info2[0].trim().toLowerCase().equals("'lastname'")) {
                    String lastName = info2[1].trim().toLowerCase();
                    char firstIndex = lastName.toCharArray()[1];
                    char lastIndex = lastName.toCharArray()[lastName.length()-2];
                    for (Map<String, Object> i : data) {
                        if (firstIndex == '%' && lastIndex == '%') {
                            lastName = lastName.substring(1, lastName.length()-1);
                            if (i.get("lastName").toString().toLowerCase().contains(lastName)) {
                                acceptList.add(i);
                            } else {
                                refuseList.add(i);
                            }
                        } else if (firstIndex == '%') {
                            lastName = lastName.substring(1);
                            if (i.get("lastName").toString().toLowerCase().contains(lastName)) {
                                if (i.get("lastName").toString().toLowerCase().substring(i.get("lastName").toString().indexOf(lastName)).equals(lastName)) {
                                    acceptList.add(i);
                                }
                            } else {
                                refuseList.add(i);
                            }
                        } else if (lastIndex == '%') {
                            lastName = lastName.substring(0, lastName.length()-1);
                            if (i.get("lastName").toString().toLowerCase().contains(lastName)) {
                                if (i.get("lastName").toString().toLowerCase().substring(0, lastName.length()).equals(lastName)) {
                                    acceptList.add(i);
                                }
                            } else {
                                refuseList.add(i);
                            }
                        } else {
                            if (i.get("lastName").toString().toLowerCase().equals(lastName)) {
                                acceptList.add(i);
                            } else {
                                refuseList.add(i);
                            }
                        }
                    }
                } else if (!info2[0].trim().toLowerCase().equals("'id'") || !info2[0].trim().toLowerCase().equals("'active'") || !info2[0].trim().toLowerCase().equals("'cost'") || !info2[0].trim().toLowerCase().equals("'age'")) {
                    throw new WrongColumnException("Wrong column name!");
                } else {
                    throw new WrongFormatException("Wrong format for the column!");
                }
            } else if (str.contains("like")) {
                info2 = str.split("like");
                if (info2[0].trim().toLowerCase().equals("'lastname'")) {
                    String lastName = info2[1].trim();
                    char firstIndex = lastName.toCharArray()[1];
                    char lastIndex = lastName.toCharArray()[lastName.length() - 2];
                    for (Map<String, Object> i : data) {
                        if (firstIndex == '%' && lastIndex == '%') {
                            lastName = lastName.substring(1, lastName.length() - 1);
                            if (i.get("lastName").toString().contains(lastName)) {
                                acceptList.add(i);
                            } else {
                                refuseList.add(i);
                            }
                        } else if (firstIndex == '%') {
                            lastName = lastName.substring(1);
                            if (i.get("lastName").toString().contains(lastName)) {
                                if (i.get("lastName").toString().substring(i.get("lastName").toString().indexOf(lastName)).equals(lastName)) {
                                    acceptList.add(i);
                                }
                            } else {
                                refuseList.add(i);
                            }
                        } else if (lastIndex == '%') {
                            lastName = lastName.substring(0, lastName.length() - 1);
                            if (i.get("lastName").toString().contains(lastName)) {
                                if (i.get("lastName").toString().substring(0, lastName.length()).equals(lastName)) {
                                    acceptList.add(i);
                                }
                            } else {
                                refuseList.add(i);
                            }
                        } else {
                            if (i.get("lastName").toString().equals(lastName)) {
                                acceptList.add(i);
                            } else {
                                refuseList.add(i);
                            }
                        }
                    }
                } else if (!info2[0].trim().toLowerCase().equals("'id'") || !info2[0].trim().toLowerCase().equals("'active'") || !info2[0].trim().toLowerCase().equals("'cost'") || !info2[0].trim().toLowerCase().equals("'age'")) {
                    throw new WrongColumnException("Wrong column name!");
                } else {
                    throw new WrongFormatException("Wrong format for the column!");
                }
            }
        }
        acRefList.add(acceptList);
        acRefList.add(refuseList);
        return acRefList;
    }

    private LinkedHashMap<String, Object> cleanMap() {
        LinkedHashMap<String,Object> row = new LinkedHashMap<>();
        row.put("id", null);
        row.put("lastName", null);
        row.put("age", null);
        row.put("cost", null);
        row.put("active", null);
        return row;
    }
}
