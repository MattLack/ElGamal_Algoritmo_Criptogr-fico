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
	private Long a, r, p, b;
	private Long randB;
	private Long Y, G;
	private Long s;
	private Random rand = new Random();
	
	void generateKey(Long a, Long r, Long p){
		
		this.a = a;
		this.r = r;
		this.p = p;
		
		Long c1 = (long) Math.pow(r, a); 
		Long c2 = c1 % p;
		this.b = c2;
		
		//b = rª mod p
		//chaves públicas são (b,r,p)
		//chave privada é (a)
		
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
	
	
	public static long MDC(long a, long b){
	    long resto;

	    while(b != 0){
	      resto = a % b;
	      a = b;
	      b = resto;
	    }

	    return a;
	  }
	
	
	void encrypt(Long b, Long r, Long p){
		
		this.randB = (long) rand.nextInt((int) (this.p-1));
		long aux = (long) Math.pow(r, randB);
		this.s = aux%this.p;
		//s = r^ranB mod p
		
		long n = (long) Math.pow(b, this.randB);
		long x = (this.textCODIGO*n) %p;
		//y= textCODIGO* b^randB mod p
		
		this.Y = x;
		//Y será o texo cifrado
		//deve ser visivel s e Y
		
	}
	
	
	void decrypt(Long s,Long V){
		long aux = (long) Math.pow(s, (this.p)-1-this.a);
		long y = aux % this.p;
		//y = s^(p-1-a) mod p
		
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
	
	public Long getB(){
		return this.b;
	}
	
	public Long getG(){
		return this.G;
	}
	
	
	
	
	
	public static void main(String[] args){
		
		Scanner teclado = new Scanner(System.in);
		Fazendo elgamal = new Fazendo();
		Long a = (long) 14, r = (long) 2, p = (long) 2539;
		
		
		elgamal.generateKey(a, r, p);
		
		System.out.println("Chave privada: "+a);
		System.out.println("Chaves publicas: ("+elgamal.getB()+","+r+","+p+")");
		
		System.out.println("Digite as chaves publicas em respectiva ordem:");
		System.out.println("Valor para b:");
		Long valorB = (long) teclado.nextInt();
		System.out.println("Valor para r:");
		Long valorR = (long) teclado.nextInt();
		System.out.println("Valor para p:");
		Long valorP = (long) teclado.nextInt();
		
		teclado.nextLine();
		System.out.println("Digite texto a ser encriptado: ");
		String text = teclado.nextLine();
		
		elgamal.convertText(text);
		
		System.out.println(elgamal.textoCOD);
		
		elgamal.encrypt(valorB,valorR,valorP);
		
		System.out.println("Texto cifrado: ("+elgamal.getS()+","+elgamal.getY()+")");
		
		
		System.out.println(elgamal.textoCOD);
		elgamal.decrypt(elgamal.getS(), elgamal.getY());
		System.out.println(elgamal.getG());
		
		
		
	}
	
	
	

}
