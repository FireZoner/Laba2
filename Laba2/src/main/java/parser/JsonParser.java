/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package parser;

import com.fasterxml.jackson.databind.*;
import model.*;
import java.io.*;

/**
 *
 * @author zubbo
 */
public class JsonParser extends BaseParser {
   private final ObjectMapper objectMapper; 

   public JsonParser() {
       this.objectMapper = new ObjectMapper();
   }
   
    @Override
    public Mission parse(File file) throws IOException {
        JsonNode root = objectMapper.readTree(file);
        Mission mission = parseMission(root);

        if (root.has("sorcerers")) {
            JsonNode sorcerersNode = root.get("sorcerers");
            if (sorcerersNode.isArray()) {
                for (JsonNode sNode : sorcerersNode) {
                    parseSorcerer(sNode, mission);
                }
            }
        }
        
        if (root.has("techniques")) {
            JsonNode techniquesNode = root.get("techniques");
            if (techniquesNode.isArray()) {
                for (JsonNode tNode : techniquesNode) {
                    parseTechnique(tNode, mission);
                }
            }
        }
        return mission;
    }
}
