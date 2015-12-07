package esseAqui;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.Random;

public class Fazendo {
	
	private String[] alfabeto =   {"A" ,"B" ,"C" ,"D" ,"E" ,"F" ,"G" ,"H" ,"I" ,"J" ,"K" ,"L" ,"M" ,"N" ,"O" ,"P" ,"Q" ,"R" ,"S" ,"T" ,"U" ,"V" ,"W" ,"X" ,"Y" ,"Z" ," " };
	private String[] ValorAlfab = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26"};
	private ArrayList<String> textConvertido = new ArrayList<String>();
	private String textoCOD;
	private Long textCODIGO;
	private String padrao = "999";
	
	private int tamanhoText;
	private Long p, k , g , r;
	private Long randB;
	private Long Y, G;
	private Long s;
	private Random rand = new Random();
	
	//
	
	void generateKey(Long k, Long g, Long p){
		
		this.k = k;
		this.g = g;
		this.p = p;
		
		Long c1 = (long) Math.pow(g, k); 
		Long c2 = c1 % p;
		this.r = c2;
		
		//r = g^k mod p
		//chaves públicas são (r,g,p)
		//chave privada é (k)
		
	}
	

	
	void convertText(String text){
		
		text = text.toUpperCase();
		this.tamanhoText = text.length();
		
		for(int i=0;i<text.length();i++){
			for(int j=0;j<27;j++){
				if(text.substring(i, i+1).equals(alfabeto[j])){
					System.out.println();
					this.textConvertido.add(this.ValorAlfab[j]);
				}
			}
		}
		
		
		String listString = this.padrao;

		for (String s : this.textConvertido)
		{
		    listString += s ;
		}

		listString += this.padrao;
		
		this.textoCOD = listString;
		
		this.textCODIGO = Long.parseLong(this.textoCOD);
		//transforma o texto antes em string em um valor long
		
	}
	
	
	
	void encrypt(Long r, Long g, Long p){
		
		this.randB = (long) rand.nextInt((int) (this.p-1));
		long aux = (long) Math.pow(g, randB);
		this.s = aux%this.p;
		//s = g^ranB mod p
		
		long n = (long) Math.pow(r, this.randB);
		long x = (this.textCODIGO*n) %p;
		//y= textCODIGO* r^randB mod p
		
		this.Y = x;
		//Y será o texo cifrado
		//deve ser visivel s e Y
		
	}
	
	
	void decrypt(Long s,Long V){
		long w = this.p -1 -this.k;
		long aux = (long) Math.pow(s, w);
		long y = aux % this.p;
		//y = s^(p-1-k) mod p
		
		long q = y*V;
		this.G = q % p;
		// G = y*V mod p
		
	}
	
	
	public Long getS(){
		return this.s;
	}
	
	public Long getY(){
		return this.Y;
	}
	
	public Long getR(){
		return this.r;
	}
	
	public Long getG(){
		return this.G;
	}
	
	
	
	
	
	public static void main(String[] args){
		
		Scanner teclado = new Scanner(System.in);
		Fazendo elgamal = new Fazendo();
		Long k = (long) 14, g = (long) 2, p = (long) 2539;
		
		
		elgamal.generateKey(k, g, p);
		
		System.out.println("Chave privada: "+k);
		System.out.println("Chaves publicas: ("+elgamal.getR()+","+g+","+p+")");
		
		System.out.println("Digite as chaves publicas em respectiva ordem:");
		System.out.println("Valor para r:");
		Long valorR = (long) teclado.nextInt();
		System.out.println("Valor para g:");
		Long valorG = (long) teclado.nextInt();
		System.out.println("Valor para p:");
		Long valorP = (long) teclado.nextInt();
		
		teclado.nextLine();
		System.out.println("Digite texto a ser encriptado: ");
		String text = teclado.nextLine();
		
		elgamal.convertText(text);
		
		elgamal.encrypt(valorR,valorG,valorP);
		
		System.out.println("Texto cifrado: ("+elgamal.getS()+","+elgamal.getY()+")");
		
		
		System.out.println("E pra dar isso: "+elgamal.textoCOD);
		elgamal.decrypt(elgamal.getS(), elgamal.getY());
		System.out.println("Ta dando isso: "+elgamal.getG());
		
		
		
	}
	
	
	

}
