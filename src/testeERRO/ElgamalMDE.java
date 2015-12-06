package testeERRO;


import java.util.ArrayList;
import java.util.Scanner;

public class ElgamalMDE {
	
	String[] alfabeto =   {"A" ,"B" ,"C" ,"D" ,"E" ,"F" ,"G" ,"H" ,"I" ,"J" ,"K" ,"L" ,"M" ,"N" ,"O" ,"P" ,"Q" ,"R" ,"S" ,"T" ,"U" ,"V" ,"W" ,"X" ,"Y" ,"Z" ," " };
	String[] ValorAlfab = {"00","01","02","03","04","05","06","07","08","09","10","11","12","13","14","15","16","17","18","19","20","21","22","23","24","25","26"};
	ArrayList<String> textConvertido = new ArrayList<String>();
	ArrayList<String> textBlocado = new ArrayList<String>();
	int tamanhoText;
	double a, r, p, b,y;
	int R;
	
	void generateKey(double a, double r, double p){
		
		this.a = a;
		this.r = r;
		this.p = p;
		
		double c1 = Math.pow(r, a);
		double c2 = c1 % p;
		this.b = c2;
		
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
		
	}
	
	
	void encripted(){
		int n = 0;
		int x = Integer.parseInt(this.textConvertido.get(n).concat(this.textConvertido.get(n+1)));

		while(n<this.tamanhoText+1){
			if(x<p && !this.textConvertido.get(n+2).isEmpty()){               //caso1 - x
				this.textBlocado.add(n, (this.textConvertido.get(n).concat(this.textConvertido.get(n+1))) );
				n=n+2;
				if(!this.textConvertido.get(n+1).isEmpty()){
					x = Integer.parseInt(this.textConvertido.get(n).concat(this.textConvertido.get(n+1)));
					if(x<p)
						this.textBlocado.add(n, (this.textConvertido.get(n).concat(this.textConvertido.get(n+1))) );
					else
						this.textBlocado.add(n, (this.textConvertido.get(n)) );
				}else if(!this.textConvertido.get(n).isEmpty()){
					x = Integer.parseInt(this.textConvertido.get(n));
					//aqui tem q verificar se o próximo já é nulo ou não, se não for
					//repete todo o processo de novo e de novo e de novo
				}
			}else if(x<p){                                                    //caso1 <-
				this.textBlocado.add(n, (this.textConvertido.get(n).concat(this.textConvertido.get(n+1))) );
				n++;
			}
			if(x>p && !this.textConvertido.get(n+2).isEmpty()){
				this.textBlocado.add(n, this.textConvertido.get(n));
				n++;
				if(!this.textConvertido.get(n+1).isEmpty()){
					x = Integer.parseInt(this.textConvertido.get(n).concat(this.textConvertido.get(n+1)));
					n++;
					if(x<p){
						this.textBlocado.add(n, (this.textConvertido.get(n).concat(this.textConvertido.get(n+1))) );
					}else if(x>p){
						this.textBlocado.add(n, this.textConvertido.get(n));
						n++;
						//aqui tem q verificar se o próximo já é nulo ou não, se não for
						//repete todo o processo de novo e de novo e de novo
					}
				}else{
					this.textBlocado.add(n, this.textConvertido.get(n));
					n=n+2;
				}
			}else if(x>p){                                                    //caso2 <-
				this.textBlocado.add(n, this.textConvertido.get(n));
				this.textBlocado.add(n+1, this.textConvertido.get(n+1));
				n=n+2;
			}
		}
	}

	
	
	
	public static void main(String[] args){
		
		Scanner teclado = new Scanner(System.in);
		ElgamalMDE elgamal = new ElgamalMDE();
		double a = 14, r = 2, p = 2539;
		
		elgamal.generateKey(a, r, p);
		
		
		System.out.println("Digite texto a ser encriptado: ");
		String text = teclado.nextLine();
		elgamal.convertText(text);
		
		System.out.println(elgamal.textConvertido.toString());
		
		
		
		
	}
	
	

}
