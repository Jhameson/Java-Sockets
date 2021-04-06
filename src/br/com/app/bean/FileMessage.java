/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.app.bean;

import java.io.File;
import java.io.Serializable;

/**
 *
 * @author jhame
 */
public class FileMessage implements Serializable{
    private String cliente;
    private File file;

    public FileMessage(String cliente, File file) {
        this.cliente = cliente;
        this.file = file;
    }

    public FileMessage(String cliente) {
        this.cliente = cliente;
    }

    public FileMessage() {
    }

    public String getCliente() {
        return cliente;
    }

    public File getFile() {
        return file;
    }

    
}
