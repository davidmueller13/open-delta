/*******************************************************************************
 * Copyright (C) 2011 Atlas of Living Australia
 * All Rights Reserved.
 *   
 * The contents of this file are subject to the Mozilla Public
 * License Version 1.1 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a copy of
 * the License at http://www.mozilla.org/MPL/
 *   
 * Software distributed under the License is distributed on an "AS
 * IS" basis, WITHOUT WARRANTY OF ANY KIND, either express or
 * implied. See the License for the specific language governing
 * rights and limitations under the License.
 ******************************************************************************/
package au.org.ala.delta.editor.directives;

import au.org.ala.delta.editor.slotfile.model.DirectiveFile;
import au.org.ala.delta.editor.slotfile.model.DirectiveFile.DirectiveType;
import au.org.ala.delta.editor.slotfile.model.SlotFileDataSet;
import au.org.ala.delta.model.Attribute;
import au.org.ala.delta.model.Character;
import au.org.ala.delta.model.CharacterType;
import au.org.ala.delta.model.IntegerCharacter;
import au.org.ala.delta.model.Item;
import au.org.ala.delta.model.MultiStateCharacter;
import org.junit.Test;

import java.io.File;
import java.util.Arrays;
import java.util.List;

/**
 * Tests the ImportController class. 
 */
public class ImportControllerTest extends AbstractImportControllerTest {

	
	protected void createDataSet() throws Exception {
		_dataSet = (SlotFileDataSet)_repository.newDataSet();
	}

	@Test
	public void testSilentImport() throws Exception {
		
		File datasetDirectory = new File(getClass().getResource("/dataset").toURI());
		DirectiveFileInfo specs = new DirectiveFileInfo("specs", DirectiveType.CONFOR);
		DirectiveFileInfo chars = new DirectiveFileInfo("chars", DirectiveType.CONFOR);
		DirectiveFileInfo items = new DirectiveFileInfo("items", DirectiveType.CONFOR);
		
		List<DirectiveFileInfo> files = Arrays.asList(new DirectiveFileInfo[] {specs, chars, items});
		
		importer.new DoImportTask(datasetDirectory, files, true).doInBackground();
		
		assertEquals(89, _dataSet.getNumberOfCharacters());
		// do a few random assertions
		Character character = _dataSet.getCharacter(10);
		assertEquals(10, character.getCharacterId());
		assertEquals("<adaxial> ligule <presence>", character.getDescription());
		assertEquals(CharacterType.UnorderedMultiState, character.getCharacterType());
		MultiStateCharacter multiStateChar = (MultiStateCharacter)character;
		assertEquals(2, multiStateChar.getNumberOfStates());
		assertEquals("<consistently> present <<implicit>>", multiStateChar.getState(1));
		assertEquals("absent <at least from upper leaves>", multiStateChar.getState(2));
		
		character = _dataSet.getCharacter(48);
		assertEquals("awns <of female-fertile lemmas, if present, number>", character.getDescription());
		assertEquals(CharacterType.IntegerNumeric, character.getCharacterType());
		assertEquals(48, character.getCharacterId());
		
		character = _dataSet.getCharacter(85);
		assertEquals(85, character.getCharacterId());
		assertEquals("<number of species>", character.getDescription());
		assertEquals(CharacterType.IntegerNumeric, character.getCharacterType());
		IntegerCharacter integerCharacter = (IntegerCharacter)character;
		assertEquals("species", integerCharacter.getUnits());
		
		
		assertEquals(14, _dataSet.getMaximumNumberOfItems());
		
		Item item = _dataSet.getItem(5);
		assertEquals(5, item.getItemNumber());
		
		// At the moment getDescription() strips RTF... probably should leave that to the formatter.
		//assertEquals("\\i{}Cynodon\\i0{} <Rich.>", item.getDescription());
		
		assertEquals("\\i{}Cynodon\\i0{} <Rich.>", item.getDescription());
		assertEquals("4-60(-100)", item.getAttribute(_dataSet.getCharacter(2)).getValueAsString());
		assertEquals("3", item.getAttribute(_dataSet.getCharacter(60)).getValueAsString()); 
		
		character = _dataSet.getCharacter(11);
		assertEquals("<adaxial> ligule <form; avoid seedlings>", character.getDescription());
		multiStateChar = (MultiStateCharacter)character;
		assertEquals(4, multiStateChar.getNumberOfStates());
		assertEquals("an unfringed membrane <may be variously hairy or ciliolate>", multiStateChar.getState(1));
		assertEquals("a fringed membrane", multiStateChar.getState(2));
		assertEquals("a fringe of hairs", multiStateChar.getState(3));
		assertEquals("a rim of minute papillae", multiStateChar.getState(4));	
	}
	
	@Test
	public void testToIntImport() throws Exception {

        int numChars = 89;
        for (int i=0; i<numChars; i++) {
            _dataSet.addCharacter(CharacterType.UnorderedMultiState);
        }
        // Recreate the importer after adding characters to the model as this will update the chars and items
        // in the context.
        importer = new ImportController(_helper, _model);


        String toIntPath = "/au/org/ala/delta/editor/directives/expected_results";
		File datasetDirectory = new File(getClass().getResource(toIntPath).toURI());
		DirectiveFileInfo toint = new DirectiveFileInfo("toint", DirectiveType.CONFOR);
		
		List<DirectiveFileInfo> files = Arrays.asList(new DirectiveFileInfo[] {toint});
		
		importer.new DoImportTask(datasetDirectory, files, true).doInBackground();

		assertEquals(1, _dataSet.getDirectiveFileCount());
		
		DirectiveFile file = _dataSet.getDirectiveFile(1);
		
		assertEquals(24, file.getDirectiveCount());
	}


    /**
     * Tests that the editor can import a data set with attribute data for a single Item which is larger than
     * a single slot.
     */
    @Test
    public void testLargeAttributeData() throws Exception {
        String expectedAttribute = "<\\i{}Aaronsohnia\\i0{}, " +
                "\\i{}Abrotanella\\i0{}, \\i{}Acamptopappus\\i0{}, " +
                "\\i{}Acanthocephalus\\i0{}, \\i{}Acanthocladium\\i0{}, " +
                "\\i{}Acanthodesmos\\i0{}, \\i{}Acantholepis\\i0{}, " +
                "\\i{}Acanthospermum\\i0{}, \\i{}Acanthostyles\\i0{}, \\i{}Achillea\\i0{}, " +
                "\\i{}Achnophora\\i0{}, \\i{}Achnopogon\\i0{}, \\i{}Achyrachaena\\i0{}, " +
                "\\i{}Achyrocline\\i0{}, \\i{}Achyropappus\\i0{}, \\i{}Achyrothalamus\\i0{}, " +
                "\\i{}Acilepidopsis\\i0{}, \\i{}Acilepis\\i0{}, \\i{}Acmella\\i0{}, " +
                "\\i{}Acomis\\i0{}, \\i{}Acourtia\\i0{}, \\i{}Acrisione\\i0{}, " +
                "\\i{}Acritopappus\\i0{}, \\i{}Acroptilon\\i0{}, \\i{}Actinobole\\i0{}, " +
                "\\i{}Actinoseris\\i0{}, \\i{}Actites\\i0{}, \\i{}Adelostigma\\i0{}, " +
                "\\i{}Adenanthellum\\i0{}, \\i{}Adenocaulon\\i0{}, \\i{}Adenocritonia\\i0{}, " +
                "\\i{}Adenoglossa\\i0{}, \\i{}Adenoon\\i0{}, \\i{}Adenopappus\\i0{}, " +
                "\\i{}Adenophyllum\\i0{}, \\i{}Adenostemma\\i0{}, \\i{}Adenothamnus\\i0{}, " +
                "\\i{}Aedesia\\i0{}, \\i{}Aegopordon\\i0{}, \\i{}Aequatorium\\i0{}, " +
                "\\i{}Aetheolaena\\i0{}, \\i{}Aetheorhiza\\i0{}, \\i{}Ageratella\\i0{}, " +
                "\\i{}Ageratina\\i0{}, \\i{}Ageratinastrum\\i0{}, \\i{}Ageratum\\i0{}, " +
                "\\i{}Agoseris\\i0{}, \\i{}Agrianthus\\i0{}, \\i{}Ainsliaea\\i0{}, " +
                "\\i{}Ajania\\i0{}, \\i{}Ajaniopsis\\i0{}, \\i{}Alatoseta\\i0{}, " +
                "\\i{}Albertinia\\i0{}, \\i{}Alcantara\\i0{}, \\i{}Alciope\\i0{}, " +
                "\\i{}Aldama\\i0{}, \\i{}Alepidocline\\i0{}, \\i{}Alfredia\\i0{}, " +
                "\\i{}Aliella\\i0{}, \\i{}Allagopappus\\i0{}, \\i{}Allardia\\i0{}, " +
                "\\i{}Alloispermum\\i0{}, \\i{}Allopterigeron\\i0{}, \\i{}Almutaster\\i0{}, " +
                "\\i{}Alomia\\i0{}, \\i{}Alomiella\\i0{}, \\i{}Alvordia\\i0{}, " +
                "\\i{}Amauria\\i0{}, \\i{}Amberboa\\i0{}, \\i{}Amblyocarpum\\i0{}, " +
                "\\i{}Amblyolepis\\i0{}, \\i{}Amblyopappus\\i0{}, \\i{}Amboroa\\i0{}, " +
                "\\i{}Ambrosia\\i0{}, \\i{}Ameghinoa\\i0{}, \\i{}Amellus\\i0{}, " +
                "\\i{}Ammobium\\i0{}, \\i{}Amolinia\\i0{}, \\i{}Ampelaster\\i0{}, " +
                "\\i{}Amphiachyris\\i0{}, \\i{}Amphiglossa\\i0{}, \\i{}Amphipappus\\i0{}, " +
                "\\i{}Amphoricarpos\\i0{}, \\i{}Anacantha\\i0{}, \\i{}Anacyclus\\i0{}, " +
                "\\i{}Anaphalioides\\i0{}, \\i{}Anaphalis\\i0{}, \\i{}Anaxeton\\i0{}, " +
                "\\i{}Ancathia\\i0{}, \\i{}Ancistrocarphus\\i0{}, \\i{}Ancistrophora\\i0{}, " +
                "\\i{}Andryala\\i0{}, \\i{}Angelphytum\\i0{}, \\i{}Angianthus\\i0{}, " +
                "\\i{}Anisochaeta\\i0{}, \\i{}Anisocoma\\i0{}, \\i{}Anisopappus\\i0{}, " +
                "\\i{}Anisothrix\\i0{}, \\i{}Antennaria\\i0{}, \\i{}Anthemis\\i0{}, " +
                "\\i{}Antillia\\i0{}, \\i{}Antiphiona\\i0{}, \\i{}Antithrixia\\i0{}, " +
                "\\i{}Anura\\i0{}, \\i{}Anvillea\\i0{}, \\i{}Apalochlamys\\i0{}, " +
                "\\i{}Aphanactis\\i0{}, \\i{}Aphanostephus\\i0{}, \\i{}Aphyllocladus\\i0{}, " +
                "\\i{}Apodocephala\\i0{}, \\i{}Apopyros\\i0{}, \\i{}Aposeris\\i0{}, " +
                "\\i{}Apostates\\i0{}, \\i{}Arbelaezaster\\i0{}, \\i{}Archibaccharis\\i0{}, " +
                "\\i{}Arctanthemum\\i0{}, \\i{}Arctium\\i0{}, \\i{}Arctogeron\\i0{}, " +
                "\\i{}Arctotheca\\i0{}, \\i{}Arctotis\\i0{}, \\i{}Argyranthemum\\i0{}, " +
                "\\i{}Argyroglottis\\i0{}, \\i{}Argyrovernonia\\i0{}, " +
                "\\i{}Argyroxiphium\\i0{}, \\i{}Aristeguietia\\i0{}, \\i{}Arnaldoa\\i0{}, " +
                "\\i{}Arnica\\i0{}, \\i{}Arnicastrum\\i0{}, \\i{}Arnoglossum\\i0{}, " +
                "\\i{}Arnoseris\\i0{}, \\i{}Arrhenechthites\\i0{}, \\i{}Arrojadocharis\\i0{}, " +
                "\\i{}Arrowsmithia\\i0{}, \\i{}Artemisia\\i0{}, \\i{}Artemisiopsis\\i0{}, " +
                "\\i{}Asaemia\\i0{}, \\i{}Asanthus\\i0{}, \\i{}Ascidiogyne\\i0{}, " +
                "\\i{}Aspilia\\i0{}, \\i{}Asplundianthus\\i0{}, \\i{}Aster\\i0{}, " +
                "\\i{}Asteridea\\i0{}, \\i{}Asteriscus\\i0{}, \\i{}Asteromoea\\i0{}, " +
                "\\i{}Asteropsis\\i0{}, \\i{}Asterothamnus\\i0{}, \\i{}Astranthium\\i0{}, " +
                "\\i{}Athanasia\\i0{}, \\i{}Athrixia\\i0{}, \\i{}Athroisma\\i0{}, " +
                "\\i{}Atractylis\\i0{}, \\i{}Atractylodes\\i0{}, \\i{}Atrichantha\\i0{}, " +
                "\\i{}Atrichoseris\\i0{}, \\i{}Austrobrickellia\\i0{}, " +
                "\\i{}Austrocritonia\\i0{}, \\i{}Austroeupatorium\\i0{}, " +
                "\\i{}Austrosynotis\\i0{}, \\i{}Axiniphyllum\\i0{}, \\i{}Ayapana\\i0{}, " +
                "\\i{}Ayapanopsis\\i0{}, \\i{}Aylacophora\\i0{}, \\i{}Aynia\\i0{}, " +
                "\\i{}Aztecaster\\i0{}, \\i{}Baccharidopsis\\i0{}, \\i{}Baccharis\\i0{}, " +
                "\\i{}Baccharoides\\i0{}, \\i{}Badilloa\\i0{}, \\i{}Baeriopsis\\i0{}, " +
                "\\i{}Bafutia\\i0{}, \\i{}Bahia\\i0{}, \\i{}Bahianthus\\i0{}, " +
                "\\i{}Baileya\\i0{}, \\i{}Balduina\\i0{}, \\i{}Balsamorhiza\\i0{}, " +
                "\\i{}Baltimora\\i0{}, \\i{}Barkleyanthus\\i0{}, \\i{}Barnadesia\\i0{}, " +
                "\\i{}Barroetea\\i0{}, \\i{}Barrosoa\\i0{}, \\i{}Bartlettia\\i0{}, " +
                "\\i{}Bartlettina\\i0{}, \\i{}Basedowia\\i0{}, \\i{}Bebbia\\i0{}, " +
                "\\i{}Bedfordia\\i0{}, \\i{}Bejaranoa\\i0{}, \\i{}Bellida\\i0{}, " +
                "\\i{}Bellis\\i0{}, \\i{}Bellium\\i0{}, \\i{}Belloa\\i0{}, \\i{}Berardia\\i0{}, " +
                "\\i{}Berkheya\\i0{}, \\i{}Berlandiera\\i0{}, \\i{}Berroa\\i0{}, " +
                "\\i{}Berylsimpsonia\\i0{}, \\i{}Bidens\\i0{}, \\i{}Bigelowia\\i0{}, " +
                "\\i{}Bishopalea\\i0{}, \\i{}Bishopanthus\\i0{}, \\i{}Bishopiella\\i0{}, " +
                "\\i{}Bishovia\\i0{}, \\i{}Blainvillea\\i0{}, \\i{}Blakeanthus\\i0{}, " +
                "\\i{}Blakiella\\i0{}, \\i{}Blanchetia\\i0{}, \\i{}Blennosperma\\i0{}, " +
                "\\i{}Blennospora\\i0{}, \\i{}Blepharipappus\\i0{}, " +
                "\\i{}Blepharispermum\\i0{}, \\i{}Blepharizonia\\i0{}, \\i{}Blumea\\i0{}, " +
                "\\i{}Blumeopsis\\i0{}, \\i{}Boeberastrum\\i0{}, \\i{}Boeberoides\\i0{}, " +
                "\\i{}Bolanosa\\i0{}, \\i{}Bolocephalus\\i0{}, \\i{}Boltonia\\i0{}, " +
                "\\i{}Bombycilaena\\i0{}, \\i{}Borkonstia\\i0{}, \\i{}Borrichia\\i0{}, " +
                "\\i{}Bothriocline\\i0{}, \\i{}Brachanthemum\\i0{}, " +
                "\\i{}Brachionostylum\\i0{}, \\i{}Brachyactis\\i0{}, \\i{}Brachyclados\\i0{}, " +
                "\\i{}Brachyglottis\\i0{}, \\i{}Brachylaena\\i0{}, \\i{}Brachyscome\\i0{}, " +
                "\\i{}Brachythrix\\i0{}, \\i{}Bracteantha\\i0{}, \\i{}Brickellia\\i0{}, " +
                "\\i{}Brickelliastrum\\i0{}, \\i{}Bryomorphe\\i0{}, \\i{}Buphthalmum\\i0{}, " +
                "\\i{}Burkartia\\i0{}, \\i{}Cabreriella\\i0{}, \\i{}Cacalia\\i0{}, " +
                "\\i{}Cacaliopsis\\i0{}, \\i{}Cacosmia\\i0{}, \\i{}Cadiscus\\i0{}, " +
                "\\i{}Caesulia\\i0{}, \\i{}Calea\\i0{}, \\i{}Calendula\\i0{}, " +
                "\\i{}Callicephalus\\i0{}, \\i{}Callilepis\\i0{}, \\i{}Callistephus\\i0{}, " +
                "\\i{}Calocephalus\\i0{}, \\i{}Calomeria\\i0{}, \\i{}Calostephane\\i0{}, " +
                "\\i{}Calotesta\\i0{}, \\i{}Calotis\\i0{}, \\i{}Calycadenia\\i0{}, " +
                "\\i{}Calycoseris\\i0{}, \\i{}Calyptocarpus\\i0{}, \\i{}Camchaya\\i0{}, " +
                "\\i{}Campovassouria\\i0{}, \\i{}Camptacra\\i0{}, \\i{}Campuloclinium\\i0{}, " +
                "\\i{}Canadanthus\\i0{}, \\i{}Cancrinia\\i0{}, \\i{}Cancriniella\\i0{}, " +
                "\\i{}Cardopatium\\i0{}, \\i{}Carduncellus\\i0{}, \\i{}Carduus\\i0{}, " +
                "\\i{}Carlina\\i0{}, \\i{}Carminatia\\i0{}, \\i{}Carpesium\\i0{}, " +
                "\\i{}Carphephorus\\i0{}, \\i{}Carphochaete\\i0{}, \\i{}Carramboa\\i0{}, " +
                "\\i{}Carterothamnus\\i0{}, \\i{}Carthamus\\i0{}, \\i{}Cassinia\\i0{}, " +
                "\\i{}Castanedia\\i0{}, \\i{}Castrilanthemum\\i0{}, \\i{}Catamixis\\i0{}, " +
                "\\i{}Catananche\\i0{}, \\i{}Catatia\\i0{}, \\i{}Cavalcantia\\i0{}, " +
                "\\i{}Cavea\\i0{}, \\i{}Celmisia\\i0{}, \\i{}Centaurea\\i0{}, " +
                "\\i{}Centaurodendron\\i0{}, \\i{}Centauropsis\\i0{}, " +
                "\\i{}Centaurothamnus\\i0{}, \\i{}Centipeda\\i0{}, \\i{}Centratherum\\i0{}, " +
                "\\i{}Cephalipterum\\i0{}, \\i{}Cephalopappus\\i0{}, " +
                "\\i{}Cephalorrhynchus\\i0{}, \\i{}Cephalosorus\\i0{}, \\i{}Ceratogyne\\i0{}, " +
                "\\i{}Ceruana\\i0{}, \\i{}Chacoa\\i0{}, \\i{}Chaenactis\\i0{}, " +
                "\\i{}Chaetadelpha\\i0{}, \\i{}Chaetanthera\\i0{}, \\i{}Chaetopappa\\i0{}, " +
                "\\i{}Chaetoseris\\i0{}, \\i{}Chamaechaenactis\\i0{}, \\i{}Chamaegeron\\i0{}, " +
                "\\i{}Chamaemelum\\i0{}, \\i{}Chamaepus\\i0{}, \\i{}Chaptalia\\i0{}, " +
                "\\i{}Chardinia\\i0{}, \\i{}Cheirolophus\\i0{}, \\i{}Chersodoma\\i0{}, " +
                "\\i{}Chevreulia\\i0{}, \\i{}Chiliadenus\\i0{}, \\i{}Chiliocephalum\\i0{}, " +
                "\\i{}Chiliophyllum\\i0{}, \\i{}Chiliotrichiopsis\\i0{}, " +
                "\\i{}Chiliotrichum\\i0{}, \\i{}Chimantaea\\i0{}, \\i{}Chionolaena\\i0{}, " +
                "\\i{}Chionopappus\\i0{}, \\i{}Chlamydophora\\i0{}, \\i{}Chloracantha\\i0{}, " +
                "\\i{}Chondrilla\\i0{}, \\i{}Chondropyxis\\i0{}, \\i{}Chresta\\i0{}, " +
                "\\i{}Chromolaena\\i0{}, \\i{}Chromolepis\\i0{}, \\i{}Chronopappus\\i0{}, " +
                "\\i{}Chrysactinia\\i0{}, \\i{}Chrysactinium\\i0{}, " +
                "\\i{}Chrysanthellum\\i0{}, \\i{}Chrysanthemoides\\i0{}, " +
                "\\i{}Chrysanthemum\\i0{}, \\i{}Chrysanthoglossum\\i0{}, " +
                "\\i{}Chrysocephalum\\i0{}, \\i{}Chrysocoma\\i0{}, \\i{}Chrysogonum\\i0{}, " +
                "\\i{}Chrysolaena\\i0{}, \\i{}Chrysoma\\i0{}, \\i{}Chrysophthalmum\\i0{}, " +
                "\\i{}Chrysopsis\\i0{}, \\i{}Chrysothamnus\\i0{}, \\i{}Chthonocephalus\\i0{}, " +
                "\\i{}Chucoa\\i0{}, \\i{}Chuquiraga\\i0{}, \\i{}Cicerbita\\i0{}, " +
                "\\i{}Ciceronia\\i0{}, \\i{}Cichorium\\i0{}, \\i{}Cineraria\\i0{}, " +
                "\\i{}Cirsium\\i0{}, \\i{}Cissampelopsis\\i0{}, \\i{}Cladanthus\\i0{}, " +
                "\\i{}Cladochaeta\\i0{}, \\i{}Clappia\\i0{}, \\i{}Clibadium\\i0{}, " +
                "\\i{}Cnicothamnus\\i0{}, \\i{}Cnicus\\i0{}, \\i{}Coespeletia\\i0{}, " +
                "\\i{}Coleocoma\\i0{}, \\i{}Coleostephus\\i0{}, \\i{}Colobanthera\\i0{}, " +
                "\\i{}Columbiadoria\\i0{}, \\i{}Comaclinium\\i0{}, \\i{}Comborhiza\\i0{}, " +
                "\\i{}Commidendrum\\i0{}, \\i{}Complaya\\i0{}, \\i{}Condylidium\\i0{}, " +
                "\\i{}Condylopodium\\i0{}, \\i{}Conocliniopsis\\i0{}, \\i{}Conoclinium\\i0{}, " +
                "\\i{}Conyza\\i0{}, \\i{}Coreocarpus\\i0{}, \\i{}Coreopsis\\i0{}, " +
                "\\i{}Corethamnium\\i0{}, \\i{}Correllia\\i0{}, \\i{}Corymbium\\i0{}, " +
                "\\i{}Cosmos\\i0{}, \\i{}Cotula\\i0{}, \\i{}Coulterella\\i0{}, " +
                "\\i{}Cousinia\\i0{}, \\i{}Cousiniopsis\\i0{}, \\i{}Craspedia\\i0{}, " +
                "\\i{}Crassocephalum\\i0{}, \\i{}Cratystylis\\i0{}, \\i{}Cremanthodium\\i0{}, " +
                "\\i{}Crepidiastrum\\i0{}, \\i{}Crepis\\i0{}, \\i{}Critonia\\i0{}, " +
                "\\i{}Critoniadelphus\\i0{}, \\i{}Critoniella\\i0{}, \\i{}Critoniopsis\\i0{}, " +
                "\\i{}Crocidium\\i0{}, \\i{}Cronquistia\\i0{}, \\i{}Cronquistianthus\\i0{}, " +
                "\\i{}Croptilon\\i0{}, \\i{}Crossostephium\\i0{}, \\i{}Crossothamnus\\i0{}, " +
                "\\i{}Crupina\\i0{}, \\i{}Cuatrecasanthus\\i0{}, \\i{}Cuatrecasasiella\\i0{}, " +
                "\\i{}Cuchumatanea\\i0{}, \\i{}Cullumia\\i0{}, \\i{}Cuspidia\\i0{}, " +
                "\\i{}Cyanthillium\\i0{}, \\i{}Cyathocline\\i0{}, \\i{}Cyathomone\\i0{}, " +
                "\\i{}Cyclolepis\\i0{}, \\i{}Cylindrocline\\i0{}, \\i{}Cymbolaena\\i0{}, " +
                "\\i{}Cymbonotus\\i0{}, \\i{}Cymbopappus\\i0{}, \\i{}Cynara\\i0{}, " +
                "\\i{}Cyrtocymura\\i0{}, \\i{}Dacryotrichia\\i0{}, \\i{}Dahlia\\i0{}, " +
                "\\i{}Damnamenia\\i0{}, \\i{}Damnxanthodium\\i0{}, \\i{}Dasycondylus\\i0{}, " +
                "\\i{}Dasyphyllum\\i0{}, \\i{}Daveaua\\i0{}, \\i{}Decachaeta\\i0{}, " +
                "\\i{}Decastylocarpus\\i0{}, \\i{}Decazesia\\i0{}, \\i{}Delairea\\i0{}, " +
                "\\i{}Delamerea\\i0{}, \\i{}Delilia\\i0{}, \\i{}Dendranthema\\i0{}, " +
                "\\i{}Dendrocacalia\\i0{}, \\i{}Dendrophorbium\\i0{}, " +
                "\\i{}Dendrosenecio\\i0{}, \\i{}Dendroseris\\i0{}, \\i{}Denekia\\i0{}, " +
                "\\i{}Desmanthodium\\i0{}, \\i{}Dewildemania\\i0{}, \\i{}Diacranthera\\i0{}, " +
                "\\i{}Dianthoseris\\i0{}, \\i{}Diaphractanthus\\i0{}, " +
                "\\i{}Diaspananthus\\i0{}, \\i{}Dicercoclados\\i0{}, " +
                "\\i{}Dichaetophora\\i0{}, \\i{}Dichrocephala\\i0{}, " +
                "\\i{}Dichromochlamys\\i0{}, \\i{}Dicoma\\i0{}, \\i{}Dicoria\\i0{}, " +
                "\\i{}Dicranocarpus\\i0{}, \\i{}Didelta\\i0{}, \\i{}Dielitzia\\i0{}, " +
                "\\i{}Digitacalia\\i0{}, \\i{}Dimeresia\\i0{}, \\i{}Dimerostemma\\i0{}, " +
                "\\i{}Dimorphocoma\\i0{}, \\i{}Dimorphotheca\\i0{}, \\i{}Dinoseris\\i0{}, " +
                "\\i{}Diodontium\\i0{}, \\i{}Diplazoptilon\\i0{}, \\i{}Diplostephium\\i0{}, " +
                "\\i{}Dipterocome\\i0{}, \\i{}Dipterocypsela\\i0{}, \\i{}Disparago\\i0{}, " +
                "\\i{}Dissothrix\\i0{}, \\i{}Distephanus\\i0{}, \\i{}Disynaphia\\i0{}, " +
                "\\i{}Dithyrostegia\\i0{}, \\i{}Dittrichia\\i0{}, \\i{}Doellingeria\\i0{}, " +
                "\\i{}Dolichoglottis\\i0{}, \\i{}Dolichorrhiza\\i0{}, " +
                "\\i{}Dolichothrix\\i0{}, \\i{}Dolomiaea\\i0{}, \\i{}Doniophyton\\i0{}, " +
                "\\i{}Dorobaea\\i0{}, \\i{}Doronicum\\i0{}, \\i{}Dracopis\\i0{}, " +
                "\\i{}Dresslerothamnus\\i0{}, \\i{}Dubautia\\i0{}, \\i{}Dubyaea\\i0{}, " +
                "\\i{}Dugesia\\i0{}, \\i{}Duhaldea\\i0{}, \\i{}Duidaea\\i0{}, " +
                "\\i{}Duseniella\\i0{}, \\i{}Dymondia\\i0{}, \\i{}Dyscritogyne\\i0{}, " +
                "\\i{}Dyscritothamnus\\i0{}, \\i{}Dysodiopsis\\i0{}, \\i{}Dyssodia\\i0{}, " +
                "\\i{}Eastwoodia\\i0{}, \\i{}Eatonella\\i0{}, \\i{}Echinacea\\i0{}, " +
                "\\i{}Echinocoryne\\i0{}, \\i{}Echinops\\i0{}, \\i{}Eclipta\\i0{}, " +
                "\\i{}Edmondia\\i0{}, \\i{}Egletes\\i0{}, \\i{}Eirmocephala\\i0{}, " +
                "\\i{}Eitenia\\i0{}, \\i{}Ekmania\\i0{}, \\i{}Elachanthus\\i0{}, " +
                "\\i{}Elaphandra\\i0{}, \\i{}Elephantopus\\i0{}, \\i{}Eleutheranthera\\i0{}, " +
                "\\i{}Ellenbergia\\i0{}, \\i{}Elytropappus\\i0{}, \\i{}Embergeria\\i0{}, " +
                "\\i{}Emilia\\i0{}, \\i{}Emiliella\\i0{}, \\i{}Encelia\\i0{}, " +
                "\\i{}Enceliopsis\\i0{}, \\i{}Endocellion\\i0{}, \\i{}Endopappus\\i0{}, " +
                "\\i{}Engelmannia\\i0{}, \\i{}Engleria\\i0{}, \\i{}Enydra\\i0{}, " +
                "\\i{}Epaltes\\i0{}, \\i{}Epilasia\\i0{}, \\i{}Episcothamnus\\i0{}, " +
                "\\i{}Epitriche\\i0{}, \\i{}Erato\\i0{}, \\i{}Erechtites\\i0{}, " +
                "\\i{}Eremanthus\\i0{}, \\i{}Eremosis\\i0{}, \\i{}Eremothamnus\\i0{}, " +
                "\\i{}Eriachaenium\\i0{}, \\i{}Ericameria\\i0{}, \\i{}Ericentrodea\\i0{}, " +
                "\\i{}Erigeron\\i0{}, \\i{}Eriocephalus\\i0{}, \\i{}Eriochlamys\\i0{}, " +
                "\\i{}Eriophyllum\\i0{}, \\i{}Eriothrix\\i0{}, \\i{}Erlangea\\i0{}, " +
                "\\i{}Erodiophyllum\\i0{}, \\i{}Erymophyllum\\i0{}, " +
                "\\i{}Eryngiophyllum\\i0{}, \\i{}Erythradenia\\i0{}, " +
                "\\i{}Erythrocephalum\\i0{}, \\i{}Espejoa\\i0{}, \\i{}Espeletia\\i0{}, " +
                "\\i{}Espeletiopsis\\i0{}, \\i{}Ethulia\\i0{}, \\i{}Eucephalus\\i0{}, " +
                "\\i{}Euchiton\\i0{}, \\i{}Eumorphia\\i0{}, \\i{}Eupatoriastrum\\i0{}, " +
                "\\i{}Eupatorina\\i0{}, \\i{}Eupatoriopsis\\i0{}, \\i{}Eupatorium\\i0{}, " +
                "\\i{}Euphrosyne\\i0{}, \\i{}Eurybiopsis\\i0{}, \\i{}Eurydochus\\i0{}, " +
                "\\i{}Euryops\\i0{}, \\i{}Eutetras\\i0{}, \\i{}Euthamia\\i0{}, " +
                "\\i{}Evacidium\\i0{}, \\i{}Ewartia\\i0{}, \\i{}Ewartiothamnus\\i0{}, " +
                "\\i{}Exomiocarpon\\i0{}, \\i{}Facelis\\i0{}, \\i{}Farfugium\\i0{}, " +
                "\\i{}Faujasia\\i0{}, \\i{}Faxonia\\i0{}, \\i{}Feddea\\i0{}, " +
                "\\i{}Feldstonia\\i0{}, \\i{}Felicia\\i0{}, \\i{}Femeniasia\\i0{}, " +
                "\\i{}Fenixia\\i0{}, \\i{}Ferreyranthus\\i0{}, \\i{}Ferreyrella\\i0{}, " +
                "\\i{}Filago\\i0{}, \\i{}Filifolium\\i0{}, \\i{}Fitchia\\i0{}, " +
                "\\i{}Fitzwillia\\i0{}, \\i{}Flaveria\\i0{}, \\i{}Fleischmannia\\i0{}, " +
                "\\i{}Fleischmanniopsis\\i0{}, \\i{}Florestina\\i0{}, " +
                "\\i{}Floscaldasia\\i0{}, \\i{}Flosmutisia\\i0{}, \\i{}Flourensia\\i0{}, " +
                "\\i{}Flyriella\\i0{}, \\i{}Formania\\i0{}, \\i{}Foveolina\\i0{}, " +
                "\\i{}Freya\\i0{}, \\i{}Fulcaldea\\i0{}, \\i{}Gaillardia\\i0{}, " +
                "\\i{}Galactites\\i0{}, \\i{}Galeana\\i0{}, \\i{}Galeomma\\i0{}, " +
                "\\i{}Galinsoga\\i0{}, \\i{}Gamochaeta\\i0{}, \\i{}Gamochaetopsis\\i0{}, " +
                "\\i{}Garberia\\i0{}, \\i{}Garcibarrigoa\\i0{}, \\i{}Garcilassa\\i0{}, " +
                "\\i{}Gardnerina\\i0{}, \\i{}Garhadiolus\\i0{}, \\i{}Garuleum\\i0{}, " +
                "\\i{}Gazania\\i0{}, \\i{}Geigeria\\i0{}, \\i{}Geissolepis\\i0{}, " +
                "\\i{}Geraea\\i0{}, \\i{}Gerbera\\i0{}, \\i{}Geropogon\\i0{}, " +
                "\\i{}Gibbaria\\i0{}, \\i{}Gilberta\\i0{}, \\i{}Gilruthia\\i0{}, " +
                "\\i{}Gladiopappus\\i0{}, \\i{}Glaziovianthus\\i0{}, \\i{}Glossarion\\i0{}, " +
                "\\i{}Glossocardia\\i0{}, \\i{}Glossopappus\\i0{}, \\i{}Glyptopleura\\i0{}, " +
                "\\i{}Gnaphaliothamnus\\i0{}, \\i{}Gnaphalium\\i0{}, \\i{}Gnephosis\\i0{}, " +
                "\\i{}Gochnatia\\i0{}, \\i{}Goldmanella\\i0{}, \\i{}Gongrostylus\\i0{}, " +
                "\\i{}Gongylolepis\\i0{}, \\i{}Goniocaulon\\i0{}, \\i{}Gonospermum\\i0{}, " +
                "\\i{}Gorceixia\\i0{}, \\i{}Gorteria\\i0{}, \\i{}Gossweilera\\i0{}, " +
                "\\i{}Goyazianthus\\i0{}, \\i{}Grangea\\i0{}, \\i{}Grangeopsis\\i0{}, " +
                "\\i{}Graphistylis\\i0{}, \\i{}Gratwickia\\i0{}, \\i{}Grauanthus\\i0{}, " +
                "\\i{}Grazielia\\i0{}, \\i{}Greenmaniella\\i0{}, \\i{}Grindelia\\i0{}, " +
                "\\i{}Grisebachianthus\\i0{}, \\i{}Grosvenoria\\i0{}, \\i{}Guardiola\\i0{}, " +
                "\\i{}Guayania\\i0{}, \\i{}Guevaria\\i0{}, \\i{}Guizotia\\i0{}, " +
                "\\i{}Gundelia\\i0{}, \\i{}Gundlachia\\i0{}, \\i{}Gutierrezia\\i0{}, " +
                "\\i{}Gymnanthemum\\i0{}, \\i{}Gymnarrhena\\i0{}, \\i{}Gymnocondylus\\i0{}, " +
                "\\i{}Gymnocoronis\\i0{}, \\i{}Gymnodiscus\\i0{}, \\i{}Gymnolaena\\i0{}, " +
                "\\i{}Gymnopentzia\\i0{}, \\i{}Gymnosperma\\i0{}, \\i{}Gymnostephium\\i0{}, " +
                "\\i{}Gynoxys\\i0{}, \\i{}Gynura\\i0{}, \\i{}Gypothamnium\\i0{}, " +
                "\\i{}Gyptidium\\i0{}, \\i{}Gyptis\\i0{}, \\i{}Gyrodoma\\i0{}, " +
                "\\i{}Haastia\\i0{}, \\i{}Haeckeria\\i0{}, \\i{}Haegiela\\i0{}, " +
                "\\i{}Handelia\\i0{}, \\i{}Haplocarpha\\i0{}, \\i{}Haploesthes\\i0{}, " +
                "\\i{}Haplopappus\\i0{}, \\i{}Haplostephium\\i0{}, \\i{}Harleya\\i0{}, " +
                "\\i{}Harnackia\\i0{}, \\i{}Hartwrightia\\i0{}, \\i{}Hasteola\\i0{}, " +
                "\\i{}Hatschbachiella\\i0{}, \\i{}Hazardia\\i0{}, \\i{}Hebeclinium\\i0{}, " +
                "\\i{}Hecastocleis\\i0{}, \\i{}Hedypnois\\i0{}, \\i{}Helenium\\i0{}, " +
                "\\i{}Helianthella\\i0{}, \\i{}Helianthus\\i0{}, \\i{}Helichrysopsis\\i0{}, " +
                "\\i{}Helichrysum\\i0{}, \\i{}Heliocauta\\i0{}, \\i{}Heliomeris\\i0{}, " +
                "\\i{}Heliopsis\\i0{}, \\i{}Helipterum\\i0{}, \\i{}Helminthotheca\\i0{}, " +
                "\\i{}Helogyne\\i0{}, \\i{}Hemisteptia\\i0{}, \\i{}Hemizonia\\i0{}, " +
                "\\i{}Henricksonia\\i0{}, \\i{}Heptanthus\\i0{}, \\i{}Herderia\\i0{}, " +
                "\\i{}Herodotia\\i0{}, \\i{}Herrickia\\i0{}, \\i{}Hesperevax\\i0{}, " +
                "\\i{}Hesperodoria\\i0{}, \\i{}Hesperomannia\\i0{}, \\i{}Heteracia\\i0{}, " +
                "\\i{}Heteranthemis\\i0{}, \\i{}Heterocoma\\i0{}, \\i{}Heterocondylus\\i0{}, " +
                "\\i{}Heterocypsela\\i0{}, \\i{}Heteroderis\\i0{}, \\i{}Heterolepis\\i0{}, " +
                "\\i{}Heteromera\\i0{}, \\i{}Heteromma\\i0{}, \\i{}Heteropappus\\i0{}, " +
                "\\i{}Heteroplexis\\i0{}, \\i{}Heterorhachis\\i0{}, \\i{}Heterosperma\\i0{}, " +
                "\\i{}Heterothalamus\\i0{}, \\i{}Heterotheca\\i0{}, \\i{}Hidalgoa\\i0{}, " +
                "\\i{}Hieracium\\i0{}, \\i{}Hilliardia\\i0{}, \\i{}Hinterhubera\\i0{}, " +
                "\\i{}Hippia\\i0{}, \\i{}Hippolytia\\i0{}, \\i{}Hirpicium\\i0{}, " +
                "\\i{}Hispidella\\i0{}, \\i{}Hochstetteria\\i0{}, \\i{}Hoehnephytum\\i0{}, " +
                "\\i{}Hoffmanniella\\i0{}, \\i{}Hofmeisteria\\i0{}, \\i{}Holocarpha\\i0{}, " +
                "\\i{}Holocheilus\\i0{}, \\i{}Hololeion\\i0{}, \\i{}Hololepis\\i0{}, " +
                "\\i{}Holozonia\\i0{}, \\i{}Homognaphalium\\i0{}, \\i{}Homogyne\\i0{}, " +
                "\\i{}Hoplophyllum\\i0{}, \\i{}Huarpea\\i0{}, \\i{}Hubertia\\i0{}, " +
                "\\i{}Hughesia\\i0{}, \\i{}Hulsea\\i0{}, \\i{}Humeocline\\i0{}, " +
                "\\i{}Hyalis\\i0{}, \\i{}Hyalochaete\\i0{}, \\i{}Hyalochlamys\\i0{}, " +
                "\\i{}Hyaloseris\\i0{}, \\i{}Hyalosperma\\i0{}, \\i{}Hybridella\\i0{}, " +
                "\\i{}Hydroidea\\i0{}, \\i{}Hydropectis\\i0{}, \\i{}Hymenocephalus\\i0{}, " +
                "\\i{}Hymenoclea\\i0{}, \\i{}Hymenolepis\\i0{}, \\i{}Hymenonema\\i0{}, " +
                "\\i{}Hymenopappus\\i0{}, \\i{}Hymenostemma\\i0{}, \\i{}Hymenothrix\\i0{}, " +
                "\\i{}Hymenoxys\\i0{}, \\i{}Hyoseris\\i0{}, \\i{}Hypacanthium\\i0{}, " +
                "\\i{}Hypericophyllum\\i0{}, \\i{}Hypochaeris\\i0{}, \\i{}Hysterionica\\i0{}, " +
                "\\i{}Hystrichophora\\i0{}, \\i{}Ichthyothere\\i0{}, \\i{}Idiothamnus\\i0{}, " +
                "\\i{}Ifloga\\i0{}, \\i{}Ighermia\\i0{}, \\i{}Iltisia\\i0{}, \\i{}Imeria\\i0{}, " +
                "\\i{}Inezia\\i0{}, \\i{}Inula\\i0{}, \\i{}Inulanthera\\i0{}, " +
                "\\i{}Inulopsis\\i0{}, \\i{}Iocenes\\i0{}, \\i{}Iodocephalus\\i0{}, " +
                "\\i{}Iogeton\\i0{}, \\i{}Ionactis\\i0{}, \\i{}Iostephane\\i0{}, " +
                "\\i{}Iotasperma\\i0{}, \\i{}Iphiona\\i0{}, \\i{}Iphionopsis\\i0{}, " +
                "\\i{}Iranecio\\i0{}, \\i{}Irwinia\\i0{}, \\i{}Ischnea\\i0{}, " +
                "\\i{}Ismelia\\i0{}, \\i{}Isocarpha\\i0{}, \\i{}Isocoma\\i0{}, " +
                "\\i{}Isoetopsis\\i0{}, \\i{}Isostigma\\i0{}, \\i{}Iva\\i0{}, " +
                "\\i{}Ixeridium\\i0{}, \\i{}Ixeris\\i0{}, \\i{}Ixiochlamys\\i0{}, " +
                "\\i{}Ixiolaena\\i0{}, \\i{}Ixodia\\i0{}, \\i{}Jacmaia\\i0{}, " +
                "\\i{}Jaegeria\\i0{}, \\i{}Jalcophila\\i0{}, \\i{}Jaliscoa\\i0{}, " +
                "\\i{}Jamesianthus\\i0{}, \\i{}Jaramilloa\\i0{}, \\i{}Jasonia\\i0{}, " +
                "\\i{}Jaumea\\i0{}, \\i{}Jefea\\i0{}, \\i{}Jeffreya\\i0{}, \\i{}Jessea\\i0{}, " +
                "\\i{}Joseanthus\\i0{}, \\i{}Jungia\\i0{}, \\i{}Jurinea\\i0{}, " +
                "\\i{}Jurinella\\i0{}, \\i{}Kalimeris\\i0{}, \\i{}Karelinia\\i0{}, " +
                "\\i{}Karvandarina\\i0{}, \\i{}Kaschgaria\\i0{}, \\i{}Kaunia\\i0{}, " +
                "\\i{}Keysseria\\i0{}, \\i{}Kinghamia\\i0{}, \\i{}Kingianthus\\i0{}, " +
                "\\i{}Kippistia\\i0{}, \\i{}Kirkianella\\i0{}, \\i{}Kleinia\\i0{}, " +
                "\\i{}Koanophyllon\\i0{}, \\i{}Koehneola\\i0{}, \\i{}Koelpinia\\i0{}, " +
                "\\i{}Krigia\\i0{}, \\i{}Kyrsteniopsis\\i0{}, \\i{}Lachanodes\\i0{}, " +
                "\\i{}Lachnophyllum\\i0{}, \\i{}Lachnorhiza\\i0{}, \\i{}Lachnospermum\\i0{}, " +
                "\\i{}Lactacella\\i0{}, \\i{}Lactuca\\i0{}, \\i{}Lactucella\\i0{}, " +
                "\\i{}Lactucosonchus\\i0{}, \\i{}Laennecia\\i0{}, \\i{}Laestadia\\i0{}, " +
                "\\i{}Lagascea\\i0{}, \\i{}Lagedium\\i0{}, \\i{}Lagenithrix\\i0{}, " +
                "\\i{}Lagenophora\\i0{}, \\i{}Laggera\\i0{}, \\i{}Lagophylla\\i0{}, " +
                "\\i{}Lamprachaenium\\i0{}, \\i{}Lamprocephalus\\i0{}, " +
                "\\i{}Lamyropappus\\i0{}, \\i{}Lamyropsis\\i0{}, \\i{}Langebergia\\i0{}, " +
                "\\i{}Lantanopsis\\i0{}, \\i{}Lapsana\\i0{}, \\i{}Lapsanastrum\\i0{}, " +
                "\\i{}Lasianthaea\\i0{}, \\i{}Lasiocephalus\\i0{}, \\i{}Lasiolaena\\i0{}, " +
                "\\i{}Lasiopogon\\i0{}, \\i{}Lasiospermum\\i0{}, \\i{}Lasthenia\\i0{}, " +
                "\\i{}Launaea\\i0{}, \\i{}Lawrencella\\i0{}, \\i{}Layia\\i0{}, " +
                "\\i{}Lecocarpus\\i0{}, \\i{}Leibnitzia\\i0{}, \\i{}Leiboldia\\i0{}, " +
                "\\i{}Lembertia\\i0{}, \\i{}Lemoorea\\i0{}, \\i{}Leontodon\\i0{}, " +
                "\\i{}Leontopodium\\i0{}, \\i{}Lepidaploa\\i0{}, \\i{}Lepidesmia\\i0{}, " +
                "\\i{}Lepidolopha\\i0{}, \\i{}Lepidolopsis\\i0{}, \\i{}Lepidonia\\i0{}, " +
                "\\i{}Lepidophorum\\i0{}, \\i{}Lepidophyllum\\i0{}, \\i{}Lepidospartum\\i0{}, " +
                "\\i{}Lepidostephium\\i0{}, \\i{}Leptinella\\i0{}, \\i{}Leptocarpha\\i0{}, " +
                "\\i{}Leptoclinium\\i0{}, \\i{}Leptorhynchos\\i0{}, \\i{}Leptostelma\\i0{}, " +
                "\\i{}Lescaillea\\i0{}, \\i{}Lessingia\\i0{}, \\i{}Lessingianthus\\i0{}, " +
                "\\i{}Leucactinia\\i0{}, \\i{}Leucanthemella\\i0{}, " +
                "\\i{}Leucanthemopsis\\i0{}, \\i{}Leucanthemum\\i0{}, \\i{}Leucheria\\i0{}, " +
                "\\i{}Leucoblepharis\\i0{}, \\i{}Leucocyclus\\i0{}, \\i{}Leucogenes\\i0{}, " +
                "\\i{}Leucomeris\\i0{}, \\i{}Leucophyta\\i0{}, \\i{}Leucoptera\\i0{}, " +
                "\\i{}Leunisia\\i0{}, \\i{}Leuzea\\i0{}, \\i{}Leysera\\i0{}, " +
                "\\i{}Liabellum\\i0{}, \\i{}Liabum\\i0{}, \\i{}Liatris\\i0{}, " +
                "\\i{}Libanothamnus\\i0{}, \\i{}Lidbeckia\\i0{}, \\i{}Lifago\\i0{}, " +
                "\\i{}Ligularia\\i0{}, \\i{}Limbarda\\i0{}, \\i{}Lindheimera\\i0{}, " +
                "\\i{}Lipochaeta\\i0{}, \\i{}Litogyne\\i0{}, \\i{}Litothamnus\\i0{}, " +
                "\\i{}Litrisa\\i0{}, \\i{}Llerasia\\i0{}, \\i{}Logfia\\i0{}, " +
                "\\i{}Lomatozona\\i0{}, \\i{}Lonas\\i0{}, \\i{}Lopholaena\\i0{}, " +
                "\\i{}Lophopappus\\i0{}, \\i{}Lordhowea\\i0{}, \\i{}Lorentzianthus\\i0{}, " +
                "\\i{}Loricaria\\i0{}, \\i{}Lourteigia\\i0{}, \\i{}Loxothysanus\\i0{}, " +
                "\\i{}Lucilia\\i0{}, \\i{}Luciliocline\\i0{}, \\i{}Lugoa\\i0{}, " +
                "\\i{}Luina\\i0{}, \\i{}Lulia\\i0{}, \\i{}Lundellianthus\\i0{}, " +
                "\\i{}Lycapsus\\i0{}, \\i{}Lychnophora\\i0{}, \\i{}Lycoseris\\i0{}, " +
                "\\i{}Lygodesmia\\i0{}, \\i{}Machaeranthera\\i0{}, \\i{}Macowania\\i0{}, " +
                "\\i{}Macrachaenium\\i0{}, \\i{}Macraea\\i0{}, \\i{}Macroclinidium\\i0{}, " +
                "\\i{}Macropodina\\i0{}, \\i{}Macvaughiella\\i0{}, \\i{}Madagaster\\i0{}, " +
                "\\i{}Madia\\i0{}, \\i{}Mairia\\i0{}, \\i{}Malacothrix\\i0{}, " +
                "\\i{}Mallotopus\\i0{}, \\i{}Malmeanthus\\i0{}, \\i{}Malperia\\i0{}, " +
                "\\i{}Mantisalca\\i0{}, \\i{}Marasmodes\\i0{}, \\i{}Marshallia\\i0{}, " +
                "\\i{}Marshalljohnstonia\\i0{}, \\i{}Marticorenia\\i0{}, " +
                "\\i{}Matricaria\\i0{}, \\i{}Mattfeldanthus\\i0{}, \\i{}Mattfeldia\\i0{}, " +
                "\\i{}Matudina\\i0{}, \\i{}Mauranthemum\\i0{}, \\i{}Mausolea\\i0{}, " +
                "\\i{}Mecomischus\\i0{}, \\i{}Megalodonta\\i0{}, \\i{}Melampodium\\i0{}, " +
                "\\i{}Melanodendron\\i0{}, \\i{}Melanthera\\i0{}, \\i{}Merrittia\\i0{}, " +
                "\\i{}Metalasia\\i0{}, \\i{}Metastevia\\i0{}, \\i{}Mexerion\\i0{}, " +
                "\\i{}Mexianthus\\i0{}, \\i{}Micractis\\i0{}, \\i{}Microcephala\\i0{}, " +
                "\\i{}Microglossa\\i0{}, \\i{}Microgynella\\i0{}, \\i{}Microliabum\\i0{}, " +
                "\\i{}Micropsis\\i0{}, \\i{}Micropus\\i0{}, \\i{}Microseris\\i0{}, " +
                "\\i{}Microspermum\\i0{}, \\i{}Mikania\\i0{}, \\i{}Mikaniopsis\\i0{}, " +
                "\\i{}Milleria\\i0{}, \\i{}Millotia\\i0{}, \\i{}Minuria\\i0{}, " +
                "\\i{}Miricacalia\\i0{}, \\i{}Miyamayomena\\i0{}, \\i{}Mniodes\\i0{}, " +
                "\\i{}Monactis\\i0{}, \\i{}Monarrhenus\\i0{}, \\i{}Monogereion\\i0{}, " +
                "\\i{}Monolopia\\i0{}, \\i{}Monoptilon\\i0{}, \\i{}Montanoa\\i0{}, " +
                "\\i{}Monticalia\\i0{}, \\i{}Moonia\\i0{}, \\i{}Moquinia\\i0{}, " +
                "\\i{}Morithamnus\\i0{}, \\i{}Moscharia\\i0{}, \\i{}Msuata\\i0{}, " +
                "\\i{}Mulgedium\\i0{}, \\i{}Munnozia\\i0{}, \\i{}Munzothamnus\\i0{}, " +
                "\\i{}Muschleria\\i0{}, \\i{}Mutisia\\i0{}, \\i{}Mycelis\\i0{}, " +
                "\\i{}Myopordon\\i0{}, \\i{}Myriactis\\i0{}, \\i{}Myriocephalus\\i0{}, " +
                "\\i{}Myripnois\\i0{}, \\i{}Myxopappus\\i0{}, \\i{}Nabalus\\i0{}, " +
                "\\i{}Nananthea\\i0{}, \\i{}Nannoglottis\\i0{}, \\i{}Nanothamnus\\i0{}, " +
                "\\i{}Nardophyllum\\i0{}, \\i{}Narvalina\\i0{}, \\i{}Nassauvia\\i0{}, " +
                "\\i{}Nauplius\\i0{}, \\i{}Neblinaea\\i0{}, \\i{}Neja\\i0{}, " +
                "\\i{}Nelsonianthus\\i0{}, \\i{}Nemosenecio\\i0{}, \\i{}Neocabreria\\i0{}, " +
                "\\i{}Neocuatrecasia\\i0{}, \\i{}Neohintonia\\i0{}, \\i{}Neojeffreya\\i0{}, " +
                "\\i{}Neomirandea\\i0{}, \\i{}Neomolina\\i0{}, \\i{}Neopallasia\\i0{}, " +
                "\\i{}Neotysonia\\i0{}, \\i{}Nesomia\\i0{}, \\i{}Nestlera\\i0{}, " +
                "\\i{}Neurolaena\\i0{}, \\i{}Neurolakis\\i0{}, \\i{}Nicolasia\\i0{}, " +
                "\\i{}Nicolletia\\i0{}, \\i{}Nidorella\\i0{}, \\i{}Nikitinia\\i0{}, " +
                "\\i{}Nipponanthemum\\i0{}, \\i{}Nivellea\\i0{}, \\i{}Nolletia\\i0{}, " +
                "\\i{}Nothobaccharis\\i0{}, \\i{}Nothocalais\\i0{}, \\i{}Noticastrum\\i0{}, " +
                "\\i{}Notobasis\\i0{}, \\i{}Notoseris\\i0{}, \\i{}Nouelia\\i0{}, " +
                "\\i{}Novenia\\i0{}, \\i{}Oaxacania\\i0{}, \\i{}Oblivia\\i0{}, " +
                "\\i{}Ochrocephala\\i0{}, \\i{}Oclemena\\i0{}, \\i{}Odixia\\i0{}, " +
                "\\i{}Odontocline\\i0{}, \\i{}Oedera\\i0{}, \\i{}Oiospermum\\i0{}, " +
                "\\i{}Oldenburgia\\i0{}, \\i{}Olearia\\i0{}, \\i{}Olgaea\\i0{}, " +
                "\\i{}Oligactis\\i0{}, \\i{}Oliganthes\\i0{}, \\i{}Oligocarpus\\i0{}, " +
                "\\i{}Oligochaeta\\i0{}, \\i{}Oligoneuron\\i0{}, \\i{}Oligothrix\\i0{}, " +
                "\\i{}Olivaea\\i0{}, \\i{}Omalotheca\\i0{}, \\i{}Omphalopappus\\i0{}, " +
                "\\i{}Oncosiphon\\i0{}, \\i{}Ondetia\\i0{}, \\i{}Onopordum\\i0{}, " +
                "\\i{}Onoseris\\i0{}, \\i{}Oonopsis\\i0{}, \\i{}Oparanthus\\i0{}, " +
                "\\i{}Ophryosporus\\i0{}, \\i{}Opisthopappus\\i0{}, \\i{}Oreochrysum\\i0{}, " +
                "\\i{}Oreoleysera\\i0{}, \\i{}Oreostemma\\i0{}, \\i{}Oritrophium\\i0{}, " +
                "\\i{}Orochaenactis\\i0{}, \\i{}Osbertia\\i0{}, \\i{}Osmadenia\\i0{}, " +
                "\\i{}Osmiopsis\\i0{}, \\i{}Osmitopsis\\i0{}, \\i{}Osteospermum\\i0{}, " +
                "\\i{}Otanthus\\i0{}, \\i{}Oteiza\\i0{}, \\i{}Othonna\\i0{}, " +
                "\\i{}Otopappus\\i0{}, \\i{}Otospermum\\i0{}, \\i{}Outreya\\i0{}, " +
                "\\i{}Oxycarpha\\i0{}, \\i{}Oxylaena\\i0{}, \\i{}Oxylobus\\i0{}, " +
                "\\i{}Oxypappus\\i0{}, \\i{}Oxyphyllum\\i0{}, \\i{}Oyedaea\\i0{}, " +
                "\\i{}Ozothamnus\\i0{}, \\i{}Pachylaena\\i0{}, \\i{}Pachystegia\\i0{}, " +
                "\\i{}Pachythamnus\\i0{}, \\i{}Pacifigeron\\i0{}, \\i{}Packera\\i0{}, " +
                "\\i{}Pacourina\\i0{}, \\i{}Palafoxia\\i0{}, \\i{}Paleaepappus\\i0{}, " +
                "\\i{}Pamphalea\\i0{}, \\i{}Pappobolus\\i0{}, \\i{}Pappochroma\\i0{}, " +
                "\\i{}Paracalia\\i0{}, \\i{}Parachionolaena\\i0{}, \\i{}Paragynoxys\\i0{}, " +
                "\\i{}Paralychnophora\\i0{}, \\i{}Paranephelius\\i0{}, " +
                "\\i{}Parantennaria\\i0{}, \\i{}Parapiqueria\\i0{}, " +
                "\\i{}Paraprenanthes\\i0{}, \\i{}Parasenecio\\i0{}, \\i{}Parastrephia\\i0{}, " +
                "\\i{}Parthenice\\i0{}, \\i{}Parthenium\\i0{}, \\i{}Pasaccardoa\\i0{}, " +
                "\\i{}Pechuel-Loeschea\\i0{}, \\i{}Pectis\\i0{}, \\i{}Pegolettia\\i0{}, " +
                "\\i{}Pelucha\\i0{}, \\i{}Pentacalia\\i0{}, \\i{}Pentachaeta\\i0{}, " +
                "\\i{}Pentanema\\i0{}, \\i{}Pentatrichia\\i0{}, \\i{}Pentzia\\i0{}, " +
                "\\i{}Perdicium\\i0{}, \\i{}Perezia\\i0{}, \\i{}Pericallis\\i0{}, " +
                "\\i{}Pericome\\i0{}, \\i{}Peripleura\\i0{}, \\i{}Perityle\\i0{}, " +
                "\\i{}Perralderia\\i0{}, \\i{}Pertya\\i0{}, \\i{}Perymeniopsis\\i0{}, " +
                "\\i{}Perymenium\\i0{}, \\i{}Petalacte\\i0{}, \\i{}Petasites\\i0{}, " +
                "\\i{}Peteravenia\\i0{}, \\i{}Petradoria\\i0{}, \\i{}Petrobium\\i0{}, " +
                "\\i{}Peucephyllum\\i0{}, \\i{}Phacellothrix\\i0{}, \\i{}Phaenocoma\\i0{}, " +
                "\\i{}Phaeostigma\\i0{}, \\i{}Phagnalon\\i0{}, \\i{}Phalacrachena\\i0{}, " +
                "\\i{}Phalacraea\\i0{}, \\i{}Phalacrocarpum\\i0{}, \\i{}Phalacroseris\\i0{}, " +
                "\\i{}Phaneroglossa\\i0{}, \\i{}Phanerostylis\\i0{}, \\i{}Phania\\i0{}, " +
                "\\i{}Philactis\\i0{}, \\i{}Philoglossa\\i0{}, \\i{}Philyrophyllum\\i0{}, " +
                "\\i{}Phoebanthus\\i0{}, \\i{}Phyllocephalum\\i0{}, \\i{}Phymaspermum\\i0{}, " +
                "\\i{}Picnomon\\i0{}, \\i{}Picradeniopsis\\i0{}, \\i{}Picris\\i0{}, " +
                "\\i{}Picrosia\\i0{}, \\i{}Picrothamnus\\i0{}, \\i{}Pilosella\\i0{}, " +
                "\\i{}Pilostemon\\i0{}, \\i{}Pinaropappus\\i0{}, \\i{}Pingraea\\i0{}, " +
                "\\i{}Pinillosia\\i0{}, \\i{}Piora\\i0{}, \\i{}Pippenalia\\i0{}, " +
                "\\i{}Piptocarpha\\i0{}, \\i{}Piptocoma\\i0{}, \\i{}Piptolepis\\i0{}, " +
                "\\i{}Piptothrix\\i0{}, \\i{}Piqueria\\i0{}, \\i{}Piqueriella\\i0{}, " +
                "\\i{}Piqueriopsis\\i0{}, \\i{}Pithecoseris\\i0{}, \\i{}Pithocarpa\\i0{}, " +
                "\\i{}Pittocaulon\\i0{}, \\i{}Pityopsis\\i0{}, \\i{}Pladaroxylon\\i0{}, " +
                "\\i{}Plagiobasis\\i0{}, \\i{}Plagiocheilus\\i0{}, \\i{}Plagiolophus\\i0{}, " +
                "\\i{}Plagius\\i0{}, \\i{}Planaltoa\\i0{}, \\i{}Planea\\i0{}, " +
                "\\i{}Plateilema\\i0{}, \\i{}Platycarpha\\i0{}, \\i{}Platypodanthera\\i0{}, " +
                "\\i{}Platyschkuhria\\i0{}, \\i{}Plazia\\i0{}, \\i{}Plecostachys\\i0{}, " +
                "\\i{}Plectocephalus\\i0{}, \\i{}Pleiotaxis\\i0{}, \\i{}Pleocarphus\\i0{}, " +
                "\\i{}Pleurocarpaea\\i0{}, \\i{}Pleurocoronis\\i0{}, " +
                "\\i{}Pleurophyllum\\i0{}, \\i{}Pluchea\\i0{}, \\i{}Podachaenium\\i0{}, " +
                "\\i{}Podanthus\\i0{}, \\i{}Podocoma\\i0{}, \\i{}Podolepis\\i0{}, " +
                "\\i{}Podotheca\\i0{}, \\i{}Poecilolepis\\i0{}, \\i{}Pogonolepis\\i0{}, " +
                "\\i{}Pojarkovia\\i0{}, \\i{}Pollalesta\\i0{}, \\i{}Polyachyrus\\i0{}, " +
                "\\i{}Polyanthina\\i0{}, \\i{}Polyarrhena\\i0{}, \\i{}Polycalymma\\i0{}, " +
                "\\i{}Polychrysum\\i0{}, \\i{}Polymnia\\i0{}, \\i{}Polytaxis\\i0{}, " +
                "\\i{}Porophyllum\\i0{}, \\i{}Porphyrostemma\\i0{}, \\i{}Praxeliopsis\\i0{}, " +
                "\\i{}Praxelis\\i0{}, \\i{}Prenanthella\\i0{}, \\i{}Prenanthes\\i0{}, " +
                "\\i{}Printzia\\i0{}, \\i{}Prionopsis\\i0{}, \\i{}Prolobus\\i0{}, " +
                "\\i{}Prolongoa\\i0{}, \\i{}Proteopsis\\i0{}, \\i{}Proustia\\i0{}, " +
                "\\i{}Psacaliopsis\\i0{}, \\i{}Psacalium\\i0{}, \\i{}Psathyrotes\\i0{}, " +
                "\\i{}Psathyrotopsis\\i0{}, \\i{}Psednotrichia\\i0{}, " +
                "\\i{}Pseudelephantopus\\i0{}, \\i{}Pseudobahia\\i0{}, " +
                "\\i{}Pseudoblepharisper\\i0{}, \\i{}Pseudobrickellia\\i0{}, " +
                "\\i{}Pseudocadiscus\\i0{}, \\i{}Pseudoclappia\\i0{}, " +
                "\\i{}Pseudoconyza\\i0{}, \\i{}Pseudognaphalium\\i0{}, " +
                "\\i{}Pseudogynoxys\\i0{}, \\i{}Pseudohandelia\\i0{}, " +
                "\\i{}Pseudojacobaea\\i0{}, \\i{}Pseudokyrsteniopsi\\i0{}, " +
                "\\i{}Pseudoligandra\\i0{}, \\i{}Pseudonoseris\\i0{}, " +
                "\\i{}Pseudostifftia\\i0{}, \\i{}Psiadia\\i0{}, \\i{}Psiadiella\\i0{}, " +
                "\\i{}Psilactis\\i0{}, \\i{}Psilocarphus\\i0{}, \\i{}Psilostrophe\\i0{}, " +
                "\\i{}Psychrogeton\\i0{}, \\i{}Psychrophyton\\i0{}, \\i{}Pterachaenia\\i0{}, " +
                "\\i{}Pterocaulon\\i0{}, \\i{}Pterocypsela\\i0{}, \\i{}Pteronia\\i0{}, " +
                "\\i{}Pterothrix\\i0{}, \\i{}Pterygopappus\\i0{}, \\i{}Ptilostemon\\i0{}, " +
                "\\i{}Pulicaria\\i0{}, \\i{}Pycnocephalum\\i0{}, \\i{}Pyrrhopappus\\i0{}, " +
                "\\i{}Pyrrocoma\\i0{}, \\i{}Pytinicarpa\\i0{}, \\i{}Quelchia\\i0{}, " +
                "\\i{}Quinetia\\i0{}, \\i{}Quinqueremulus\\i0{}, \\i{}Radlkoferotoma\\i0{}, " +
                "\\i{}Rafinesquia\\i0{}, \\i{}Raillardella\\i0{}, \\i{}Raillardiopsis\\i0{}, " +
                "\\i{}Rainiera\\i0{}, \\i{}Raoulia\\i0{}, \\i{}Raouliopsis\\i0{}, " +
                "\\i{}Rastrophyllum\\i0{}, \\i{}Ratibida\\i0{}, \\i{}Raulinoreitzia\\i0{}, " +
                "\\i{}Rayjacksonia\\i0{}, \\i{}Reichardia\\i0{}, \\i{}Relhania\\i0{}, " +
                "\\i{}Remya\\i0{}, \\i{}Rennera\\i0{}, \\i{}Rensonia\\i0{}, " +
                "\\i{}Revealia\\i0{}, \\i{}Rhagadiolus\\i0{}, \\i{}Rhamphogyne\\i0{}, " +
                "\\i{}Rhanteriopsis\\i0{}, \\i{}Rhanterium\\i0{}, \\i{}Rhetinolepis\\i0{}, " +
                "\\i{}Rhodanthe\\i0{}, \\i{}Rhodanthemum\\i0{}, \\i{}Rhodanthemum\\i0{}, " +
                "\\i{}Rhynchopsidium\\i0{}, \\i{}Rhynchospermum\\i0{}, \\i{}Rhysolepis\\i0{}, " +
                "\\i{}Richteria\\i0{}, \\i{}Riencourtia\\i0{}, \\i{}Rigiopappus\\i0{}, " +
                "\\i{}Robinsonecio\\i0{}, \\i{}Robinsonia\\i0{}, \\i{}Rochonia\\i0{}, " +
                "\\i{}Rojasianthe\\i0{}, \\i{}Rolandra\\i0{}, \\i{}Roldana\\i0{}, " +
                "\\i{}Rosenia\\i0{}, \\i{}Rothmaleria\\i0{}, \\i{}Rudbeckia\\i0{}, " +
                "\\i{}Rugelia\\i0{}, \\i{}Ruilopezia\\i0{}, \\i{}Rumfordia\\i0{}, " +
                "\\i{}Russowia\\i0{}, \\i{}Rutidosis\\i0{}, \\i{}Sabazia\\i0{}, " +
                "\\i{}Sachsia\\i0{}, \\i{}Salmea\\i0{}, \\i{}Santolina\\i0{}, " +
                "\\i{}Santosia\\i0{}, \\i{}Sanvitalia\\i0{}, \\i{}Sarcanthemum\\i0{}, " +
                "\\i{}Sartorina\\i0{}, \\i{}Sartwellia\\i0{}, \\i{}Saussurea\\i0{}, " +
                "\\i{}Scalesia\\i0{}, \\i{}Scariola\\i0{}, \\i{}Scherya\\i0{}, " +
                "\\i{}Schischkinia\\i0{}, \\i{}Schistocarpha\\i0{}, " +
                "\\i{}Schistostephium\\i0{}, \\i{}Schizogyne\\i0{}, \\i{}Schizoptera\\i0{}, " +
                "\\i{}Schizotrichia\\i0{}, \\i{}Schkuhria\\i0{}, \\i{}Schlechtendalia\\i0{}, " +
                "\\i{}Schmalhausenia\\i0{}, \\i{}Schoenia\\i0{}, \\i{}Sciadocephala\\i0{}, " +
                "\\i{}Sclerocarpus\\i0{}, \\i{}Sclerolepis\\i0{}, \\i{}Sclerorhachis\\i0{}, " +
                "\\i{}Sclerostephane\\i0{}, \\i{}Scolymus\\i0{}, \\i{}Scorzonera\\i0{}, " +
                "\\i{}Scrobicaria\\i0{}, \\i{}Selloa\\i0{}, \\i{}Senecio\\i0{}, " +
                "\\i{}Sericocarpus\\i0{}, \\i{}Seriphidium\\i0{}, \\i{}Serratula\\i0{}, " +
                "\\i{}Shafera\\i0{}, \\i{}Sheareria\\i0{}, \\i{}Shinnersia\\i0{}, " +
                "\\i{}Shinnersoseris\\i0{}, \\i{}Siapaea\\i0{}, \\i{}Siebera\\i0{}, " +
                "\\i{}Sigesbeckia\\i0{}, \\i{}Siloxerus\\i0{}, \\i{}Silphium\\i0{}, " +
                "\\i{}Silybum\\i0{}, \\i{}Simsia\\i0{}, \\i{}Sinacalia\\i0{}, " +
                "\\i{}Sinclairia\\i0{}, \\i{}Sinoleontopodium\\i0{}, \\i{}Sinosenecio\\i0{}, " +
                "\\i{}Sipolisia\\i0{}, \\i{}Smallanthus\\i0{}, \\i{}Soaresia\\i0{}, " +
                "\\i{}Solanecio\\i0{}, \\i{}Solenogyne\\i0{}, \\i{}Solidago\\i0{}, " +
                "\\i{}Soliva\\i0{}, \\i{}Sommerfeltia\\i0{}, \\i{}Sonchus\\i0{}, " +
                "\\i{}Sondottia\\i0{}, \\i{}Soroseris\\i0{}, \\i{}Spaniopappus\\i0{}, " +
                "\\i{}Sparganophorus\\i0{}, \\i{}Sphaeranthus\\i0{}, " +
                "\\i{}Sphaereupatorium\\i0{}, \\i{}Sphaeromeria\\i0{}, " +
                "\\i{}Sphagneticola\\i0{}, \\i{}Spilanthes\\i0{}, \\i{}Spiracantha\\i0{}, " +
                "\\i{}Spiroseris\\i0{}, \\i{}Squamopappus\\i0{}, \\i{}Stachycephalum\\i0{}, " +
                "\\i{}Staehelina\\i0{}, \\i{}Standleyanthus\\i0{}, \\i{}Staurochlamys\\i0{}, " +
                "\\i{}Stebbinsoseris\\i0{}, \\i{}Steiractinia\\i0{}, \\i{}Steirodiscus\\i0{}, " +
                "\\i{}Stemmacantha\\i0{}, \\i{}Stenachaenium\\i0{}, \\i{}Stenocephalum\\i0{}, " +
                "\\i{}Stenocline\\i0{}, \\i{}Stenopadus\\i0{}, \\i{}Stenophalium\\i0{}, " +
                "\\i{}Stenops\\i0{}, \\i{}Stenoseris\\i0{}, \\i{}Stenotus\\i0{}, " +
                "\\i{}Stephanochilus\\i0{}, \\i{}Stephanodoria\\i0{}, " +
                "\\i{}Stephanomeria\\i0{}, \\i{}Steptorhamphus\\i0{}, \\i{}Stevia\\i0{}, " +
                "\\i{}Steviopsis\\i0{}, \\i{}Steyermarkina\\i0{}, \\i{}Stifftia\\i0{}, " +
                "\\i{}Stilpnogyne\\i0{}, \\i{}Stilpnolepis\\i0{}, \\i{}Stilpnopappus\\i0{}, " +
                "\\i{}Stoebe\\i0{}, \\i{}Stokesia\\i0{}, \\i{}Stomatanthes\\i0{}, " +
                "\\i{}Stomatochaeta\\i0{}, \\i{}Stramentopappus\\i0{}, " +
                "\\i{}Streptoglossa\\i0{}, \\i{}Strotheria\\i0{}, \\i{}Stuartina\\i0{}, " +
                "\\i{}Stuckertiella\\i0{}, \\i{}Stuessya\\i0{}, \\i{}Stylocline\\i0{}, " +
                "\\i{}Stylotrichium\\i0{}, \\i{}Sventenia\\i0{}, \\i{}Symphyllocarpus\\i0{}, " +
                "\\i{}Symphyopappus\\i0{}, \\i{}Symphyotrichum\\i0{}, " +
                "\\i{}Syncalathium\\i0{}, \\i{}Syncarpha\\i0{}, \\i{}Syncephalum\\i0{}, " +
                "\\i{}Syncretocarpus\\i0{}, \\i{}Synedrella\\i0{}, \\i{}Synedrellopsis\\i0{}, " +
                "\\i{}Syneilesis\\i0{}, \\i{}Synotis\\i0{}, \\i{}Syntrichopappus\\i0{}, " +
                "\\i{}Synurus\\i0{}, \\i{}Syreitschikovia\\i0{}, \\i{}Taeckholmia\\i0{}, " +
                "\\i{}Tagetes\\i0{}, \\i{}Takeikadzuchia\\i0{}, \\i{}Takhtajaniantha\\i0{}, " +
                "\\i{}Talamancalia\\i0{}, \\i{}Tamananthus\\i0{}, \\i{}Tamania\\i0{}, " +
                "\\i{}Tamaulipa\\i0{}, \\i{}Tanacetopsis\\i0{}, \\i{}Tanacetum\\i0{}, " +
                "\\i{}Taplinia\\i0{}, \\i{}Taraxacum\\i0{}, \\i{}Tarchonanthus\\i0{}, " +
                "\\i{}Tehuana\\i0{}, \\i{}Teixeiranthus\\i0{}, \\i{}Telanthophora\\i0{}, " +
                "\\i{}Telekia\\i0{}, \\i{}Telmatophila\\i0{}, \\i{}Tenrhynea\\i0{}, " +
                "\\i{}Tephroseris\\i0{}, \\i{}Tessaria\\i0{}, \\i{}Tetrachyron\\i0{}, " +
                "\\i{}Tetradymia\\i0{}, \\i{}Tetragonotheca\\i0{}, \\i{}Tetramolopium\\i0{}, " +
                "\\i{}Tetraneuris\\i0{}, \\i{}Tetranthus\\i0{}, \\i{}Tetraperone\\i0{}, " +
                "\\i{}Thaminophyllum\\i0{}, \\i{}Thamnoseris\\i0{}, \\i{}Thelesperma\\i0{}, " +
                "\\i{}Thespidium\\i0{}, \\i{}Thespis\\i0{}, \\i{}Thevenotia\\i0{}, " +
                "\\i{}Thiseltonia\\i0{}, \\i{}Thurovia\\i0{}, \\i{}Thymophylla\\i0{}, " +
                "\\i{}Thymopsis\\i0{}, \\i{}Tiarocarpus\\i0{}, \\i{}Tietkensia\\i0{}, " +
                "\\i{}Tithonia\\i0{}, \\i{}Tolbonia\\i0{}, \\i{}Tolpis\\i0{}, " +
                "\\i{}Tomentaurum\\i0{}, \\i{}Tonestus\\i0{}, \\i{}Tourneuxia\\i0{}, " +
                "\\i{}Townsendia\\i0{}, \\i{}Tracyina\\i0{}, \\i{}Tragopogon\\i0{}, " +
                "\\i{}Traversia\\i0{}, \\i{}Trichanthemis\\i0{}, \\i{}Trichanthodium\\i0{}, " +
                "\\i{}Trichocline\\i0{}, \\i{}Trichocoronis\\i0{}, \\i{}Trichocoryne\\i0{}, " +
                "\\i{}Trichogonia\\i0{}, \\i{}Trichogoniopsis\\i0{}, \\i{}Trichogyne\\i0{}, " +
                "\\i{}Tricholepis\\i0{}, \\i{}Trichoptilium\\i0{}, \\i{}Trichospira\\i0{}, " +
                "\\i{}Tridactylina\\i0{}, \\i{}Tridax\\i0{}, \\i{}Trigonospermum\\i0{}, " +
                "\\i{}Trilisa\\i0{}, \\i{}Trimorpha\\i0{}, \\i{}Trioncinia\\i0{}, " +
                "\\i{}Tripleurospermum\\i0{}, \\i{}Triplocephalum\\i0{}, " +
                "\\i{}Tripteris\\i0{}, \\i{}Triptilion\\i0{}, \\i{}Triptilodiscus\\i0{}, " +
                "\\i{}Trixis\\i0{}, \\i{}Troglophyton\\i0{}, \\i{}Tuberostylis\\i0{}, " +
                "\\i{}Tugarinovia\\i0{}, \\i{}Turaniphytum\\i0{}, \\i{}Tussilago\\i0{}, " +
                "\\i{}Tuxtla\\i0{}, \\i{}Tyleropappus\\i0{}, \\i{}Tyrimnus\\i0{}, " +
                "\\i{}Uechtritzia\\i0{}, \\i{}Ugamia\\i0{}, \\i{}Uleophytum\\i0{}, " +
                "\\i{}Unxia\\i0{}, \\i{}Urbananthus\\i0{}, \\i{}Urbinella\\i0{}, " +
                "\\i{}Urmenetea\\i0{}, \\i{}Urolepis\\i0{}, \\i{}Uropappus\\i0{}, " +
                "\\i{}Urospermum\\i0{}, \\i{}Ursinia\\i0{}, \\i{}Vanclevea\\i0{}, " +
                "\\i{}Varilla\\i0{}, \\i{}Varthemia\\i0{}, \\i{}Vellereophyton\\i0{}, " +
                "\\i{}Venegasia\\i0{}, \\i{}Verbesina\\i0{}, \\i{}Vernonia\\i0{}, " +
                "\\i{}Vernoniopsis\\i0{}, \\i{}Viereckia\\i0{}, \\i{}Vieria\\i0{}, " +
                "\\i{}Vigethia\\i0{}, \\i{}Viguiera\\i0{}, \\i{}Villanova\\i0{}, " +
                "\\i{}Vilobia\\i0{}, \\i{}Vittadinia\\i0{}, \\i{}Vittetia\\i0{}, " +
                "\\i{}Volutaria\\i0{}, \\i{}Waitzia\\i0{}, \\i{}Wamalchitamia\\i0{}, " +
                "\\i{}Warionia\\i0{}, \\i{}Wedelia\\i0{}, \\i{}Welwitschiella\\i0{}, " +
                "\\i{}Wendelboa\\i0{}, \\i{}Werneria\\i0{}, \\i{}Westoniella\\i0{}, " +
                "\\i{}Whitneya\\i0{}, \\i{}Wilkesia\\i0{}, \\i{}Willemetia\\i0{}, " +
                "\\i{}Wollastonia\\i0{}, \\i{}Wulffia\\i0{}, \\i{}Wunderlichia\\i0{}, " +
                "\\i{}Wyethia\\i0{}, \\i{}Xanthisma\\i0{}, \\i{}Xanthium\\i0{}, " +
                "\\i{}Xanthocephalum\\i0{}, \\i{}Xanthopappus\\i0{}, \\i{}Xeranthemum\\i0{}, " +
                "\\i{}Xerolekia\\i0{}, \\i{}Xylanthemum\\i0{}, \\i{}Xylorhiza\\i0{}, " +
                "\\i{}Xylothamia\\i0{}, \\i{}Yermo\\i0{}, \\i{}Youngia\\i0{}, " +
                "\\i{}Zaluzania\\i0{}, \\i{}Zandera\\i0{}, \\i{}Zexmenia\\i0{}, " +
                "\\i{}Zinnia\\i0{}, \\i{}Zoegea\\i0{}, \\i{}Zyrphelis\\i0{}, " +
                "\\i{}Zyzyxia\\i0{}>";
        File datasetDirectory = new File(getClass().getResource("/dataset/long_attribute").toURI());
        DirectiveFileInfo specs = new DirectiveFileInfo("specs", DirectiveType.CONFOR);
        DirectiveFileInfo chars = new DirectiveFileInfo("chars", DirectiveType.CONFOR);
        DirectiveFileInfo items = new DirectiveFileInfo("items", DirectiveType.CONFOR);

        List<DirectiveFileInfo> files = Arrays.asList(new DirectiveFileInfo[] {specs, chars, items});

        importer.new DoImportTask(datasetDirectory, files, true).doInBackground();

        assertEquals(1, _dataSet.getNumberOfCharacters());
        assertEquals(1, _dataSet.getMaximumNumberOfItems());

        Attribute attribute = _dataSet.getItem(1).getAttribute(_dataSet.getCharacter(1));
        assertEquals(expectedAttribute, attribute.getValueAsString());
    }

	
}
