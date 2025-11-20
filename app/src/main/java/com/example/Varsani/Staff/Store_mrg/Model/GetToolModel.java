package com.example.Varsani.Staff.Store_mrg.Model;


public class GetToolModel {
    String toolID;
    String toolName;
    String quantity;
    String category;

    public GetToolModel(String toolID, String toolName, String quantity, String category){
        this.toolName=toolName;
        this.quantity=quantity;
        this.toolID=toolID;
        this.category=category;
    }

    public String getToolID() {
        return toolID;
    }

    public String getToolName() {
        return toolName;
    }

    public String getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

}
