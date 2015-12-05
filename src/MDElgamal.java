import java.util.*;
import java.math.*;

import javax.swing.InternalFrameFocusTraversalPolicy;

/**
 * 
 * @author Matt_lackome
 *
 */

public class MDElgamal {
	
	/**
	 * MDElgamal()
	 * construtor da classe
	 */
	
	MDElgamal(){
		
	}
	
	String[] alfabeto =   {"A" ,"B" ,"C" ,"D" ,"E" ,"F" ,"G" ,"H" ,"I" ,"J" ,"K" ,"L" ,"M" ,"N" ,"O" ,"P" ,"Q" ,"R" ,"S" ,"T" ,"U" ,"V" ,"W" ,"X" ,"Y" ,"Z" ," " };
	String[] ValorAlfab = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26"};
	String[] textConvertido;
	int tamanhoText;
	double a, r, p, b;
	int R;
	
	Random rand = new Random();
	
	
	/**
	 * convertText converte uma string lida para um padrão de valores pré-setados
	 * @param text - string repassada
	 * @return seta valores no vetor textConvertido com o padrão a se verificar
	 * 
	 * seta também o número de caracteres do texto passado em:
	 * @param this.tamanhoText
	 */
	
	void convertText(String text){
		text = text.toUpperCase();
		this.tamanhoText = text.length();
		this.textConvertido = new String[this.tamanhoText];
		for(int i=0;i<text.length();i++){
			for(int j=0;j<27;j++){
				if(text.substring(i, i+1).equals(alfabeto[j])){
					System.out.println();
					this.textConvertido[i] = this.ValorAlfab[j];
				}
			}
		}
		
	}
	
	/**
	 * generateKey seta os valores para as chaves publicas e privadas
	 * @param a - chave privada
	 * @param r - chave publica
	 * @param p - chave publica (número primo grande)
	 * b recebe a terceira chave pulica
	 */
	
	void generateKey(double a, double r, double p){
		
		this.a = a;
		this.r = r;
		this.p = p;
		
		double c1 = Math.pow(r, a);
		double c2 = c1 % p;
		this.b = c2;
		
	}
	
//	/**
//	 * tamanhoArray - método que retorna um inteiro com o tamanho
//	 * do array de texto que foi convertido
//	 * @param text - Array de texto
//	 * @return k - inteiro com o tamanho exato do numero de caracteres convertidos
//	 */
//	
//	int tamanhoArray(String[] text){
//		int k=0;
//		while(!(text[k].equals(null))){
//			k++;
//		}
//		return k;
//	}
	
	int[] encriptText(){
		
		this.R = rand.nextInt((int)(this.p - 1));
		int x = Math.round(tamanhoText/2);
		
		String[] copy = this.textConvertido;
		int[] valor = new int[x+1];
		int i=0,k=0;
		
		
		//algoritmo de concatenação
		if(i==0){
			valor[i] = Integer.parseInt((copy[i].concat(copy[i+1])));
			k=i+1;
		}while(k<tamanhoText+1){
			i++;
			if( (k+2 != tamanhoText) && ( (k+2) <= tamanhoText )){
				valor[i] = Integer.parseInt((copy[k+1].concat(copy[k+2])));
			}
			else if(k+1 < tamanhoText+1){
				valor[i] = Integer.parseInt(copy[k+1]);
			}
			k=k+2;
		}
		
		return valor;
		
	}
	
	
	
	public static void main(String[] args){
		
		Scanner teclado = new Scanner(System.in);
		
		MDElgamal ok = new MDElgamal();
		
		/**
		 * Setando valores para chaves publicas e privada:
		 * double a = 14, r = 2, p = 2539;
		 * E invocando o metodo:
		 * generateKey(a, r, p);
		 */
		
		double a = 14, r = 2, p = 2539;
		
		ok.generateKey(a, r, p);
		
		
		
		System.out.println("Digite texto a ser encriptado: ");
		String text = teclado.nextLine();
		
		/**
		 * invocando o método convertText
		 * para transformar o texto lido no padrão
		 * pré-definido
		 */
		
		ok.convertText(text);
		
		
		
		int[] n = ok.encriptText();
		
		for(int i=0;i<3;i++){
			System.out.println(n[i]);
		}
	}
		
}


