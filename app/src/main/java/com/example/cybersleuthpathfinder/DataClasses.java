package com.example.cybersleuthpathfinder;

import java.util.ArrayList;
import java.util.List;

class DstDigimon {
    String SP;
    String Name;
    String ATK;
    String CAM;
    String Level;
    String DEF;
    String Item;
    String Digimon;
    String special;
    String String;
    String HP;
    String ABI;
    String SPD;
}

class Skill {
    String Name;
    ArrayList<Learns> Digimon;
}

class Learns {
    String Name;
    String Level;
}

class SrcDigimon {
    String Name;
    List<String> Prev;
    List<DstDigimon> Next;
    List<Learns> Skills;
}

class Digimon {
    String Id;
    String Name;
    String Stage;
    String Type;
    String Attribute;
    String Memory;
    String EquipSlots;
    String HP;
    String SP;
    String Atk;
    String Def;
    String Int;
    String Spd;
}