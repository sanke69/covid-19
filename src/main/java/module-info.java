module javafr.covid19 {
	requires javafx.graphics;

	requires com.fasterxml.jackson.databind;

	requires transitive javafr.outbreak;
	requires javafx.controls;

	exports fr.covid19;

	// For debug only... Eclipse issues with java modules and maven
	exports fr.main.tests;
	exports fr.main.covid19;

}