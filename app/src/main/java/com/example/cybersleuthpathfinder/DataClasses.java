package com.example.cybersleuthpathfinder;

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

class DstSkill {
    String Name;
    String Level;
}

class SrcDigimon {
    String Name;
    List<String> Prev;
    List<DstDigimon> Next;
    List<DstSkill> Skills;
}