package com.quickrant.rave.model;

import org.javalite.activejdbc.Model;
import org.javalite.activejdbc.annotations.IdGenerator;

@IdGenerator("nextval('rants_id_seq')")
public class Rant extends Model {
	 
}