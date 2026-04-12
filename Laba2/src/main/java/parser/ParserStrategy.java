/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package parser;

import model.*;
import builders.MissionBuilder;
import java.io.*;

/**
 * @author zubbo
 */
public interface ParserStrategy {
    boolean supports(File file);
    Mission parse(File file, MissionBuilder builder) throws IOException;
}