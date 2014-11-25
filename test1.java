/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package ErrorCorrection;

import org.biojava3.alignment.Alignments;
import org.biojava3.alignment.Alignments.PairwiseSequenceAlignerType;
import org.biojava3.alignment.SimpleGapPenalty;
import org.biojava3.alignment.SubstitutionMatrixHelper;
import org.biojava3.alignment.template.SequencePair;
import org.biojava3.alignment.template.SubstitutionMatrix;
import org.biojava3.core.sequence.DNASequence;
import org.biojava3.core.sequence.compound.AmbiguityDNACompoundSet;
import org.biojava3.core.sequence.compound.NucleotideCompound;

/**
 *
 * @author kki8
 */
public class test1 {
public static void main(String[] args){
		String targetSeq = "CACGTTTCTTGTGGCAGCTTAAGTTTGAATGTCATTTCTTCAATGGGACGGA"+
		          "GCGGGTGCGGTTGCTGGAAAGATGCATCTATAACCAAGAGGAGTCCGTGCGCTTCGACAGC"+
			  "GACGTGGGGGAGTACCGGGCGGTGACGGAGCTGGGGCGGCCTGATGCCGAGTACTGGAACA"+
			  "GCCAGAAGGACCTCCTGGAGCAGAGGCGGGCCGCGGTGGACACCTACTGCAGACACAACTA"+ 
			  "CGGGGTTGGTGAGAGCTTCACAGTGCAGCGGCGAG";
		DNASequence target = new DNASequence(targetSeq,
				AmbiguityDNACompoundSet.getDNACompoundSet());
 
		String querySeq = "ACGAGTGCGTGTTTTCCCGCCTGGTCCCCAGGCCCCCTTTCCGTCCTCAGGAA"+
			  "GACAGAGGAGGAGCCCCTCGGGCTGCAGGTGGTGGGCGTTGCGGCGGCGGCCGGTTAAGGT"+
			  "TCCCAGTGCCCGCACCCGGCCCACGGGAGCCCCGGACTGGCGGCGTCACTGTCAGTGTCTT"+
			  "CTCAGGAGGCCGCCTGTGTGACTGGATCGTTCGTGTCCCCACAGCACGTTTCTTGGAGTAC"+
			  "TCTACGTCTGAGTGTCATTTCTTCAATGGGACGGAGCGGGTGCGGTTCCTGGACAGATACT"+
			  "TCCATAACCAGGAGGAGAACGTGCGCTTCGACAGCGACGTGGGGGAGTTCCGGGCGGTGAC"+
			  "GGAGCTGGGGCGGCCTGATGCCGAGTACTGGAACAGCCAGAAGGACATCCTGGAAGACGAG"+
			  "CGGGCCGCGGTGGACACCTACTGCAGACACAACTACGGGGTTGTGAGAGCTTCACCGTGCA"+ 
			  "GCGGCGAGACGCACTCGT";
		DNASequence query = new DNASequence(querySeq,
				AmbiguityDNACompoundSet.getDNACompoundSet());
 
		SubstitutionMatrix<NucleotideCompound> matrix = SubstitutionMatrixHelper.getNuc4_4();
 
		SimpleGapPenalty gapP = new SimpleGapPenalty();
		gapP.setOpenPenalty((short)5);
		gapP.setExtensionPenalty((short)2);
 
		SequencePair<DNASequence, NucleotideCompound> psa =
				Alignments.getPairwiseAlignment(query, target,PairwiseSequenceAlignerType.LOCAL, gapP, matrix);
 
		System.out.println(psa);
	}

}
