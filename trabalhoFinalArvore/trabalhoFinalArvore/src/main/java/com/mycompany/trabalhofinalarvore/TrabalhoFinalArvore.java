
package com.mycompany.trabalhofinalarvore;
import java.util.Scanner;


class Tnodo {
    Tnodo primeiro;
    Tnodo segundo;
    Tnodo terceiro;
    Tnodo quarto;
    Tespecie avlRaiz;
}

class Tespecie {
    String nomenclatura;
    String nomePopular;
    String bioma;
    String grauAmeaca;
    Tespecie esquerda;
    Tespecie direita;
    int altura;
}

public class TrabalhoFinalArvore {
    
    //criação de nodo separada
    static Tespecie criarEspecie(Scanner scanner){
        Tespecie novo = new Tespecie();
        System.out.println("\nDigite o nome cientifico de sua especie: ");
        novo.nomenclatura = scanner.nextLine();
        System.out.println("\nDigite o nome popular da especie (se tiver)");
        novo.nomePopular = scanner.nextLine();
        System.out.println("\nDigite o bioma que sua especie habita: ");
        novo.bioma = scanner.nextLine();
        System.out.println("\nDigite a sigla do grau de ameaca da sua especie:\nQuase ameacada NT\nVulneravel VU\nEm perigo EN\nCriticamente em perigo CR\nExtinto na natureza EW\nExtinto EX");
        novo.grauAmeaca = scanner.nextLine();
        novo.altura = 0;
        
        return novo;
    }
    //saber onde ele pertence
    static Tnodo categorizarEspecie(Tnodo inicio, Scanner scanner){
        
        Tnodo w = inicio;
        
        System.out.println("\nVertebrados(1) ou Invertebrados(2)");
        int tecla = scanner.nextInt();
        scanner.nextLine();
        switch(tecla){
            case 1: w = w.primeiro;
                break;
            case 2: w = w.segundo;
                break;
            default: System.out.println("\nOpcao invalida. Por favor tente novamente.");
                break;
        }
        
        System.out.println("\nGrupo: Terrestres(1), Aquaticos(2), Voadores(3) ou Anfibios(4)?");
        tecla = scanner.nextInt();
        scanner.nextLine();
        switch(tecla){
            case 1: w = w.primeiro;
                break;
            case 2: w = w.segundo;
                break;
            case 3: w = w.terceiro;
                break;
            case 4: w = w.quarto;
                break;
            default: System.out.println("\nOpcao invalida. Por favor tente novamente.");
                break;
        }
        
        return w;
    }
    //inserção
    static Tespecie inserirEspecie(Tespecie inicio, Tespecie novo){
        
        //insere de acordo com a ordem alfabetica (BST so que de letra)
        if (inicio == null){
            inicio = novo;
        } else if (novo.nomePopular.compareTo(inicio.nomePopular) < 0){
            inicio.esquerda = inserirEspecie(inicio.esquerda, novo);
        } else if (novo.nomePopular.compareTo(inicio.nomePopular) > 0){
            inicio.direita = inserirEspecie(inicio.direita, novo);
        } else {
            System.out.println("\nNome tem a mesma ordem alfabética, duplicata. Tente outra especie.");
            return inicio;
        }
        
        //ajustar e descobrir se precisa rotacionar
        definirAltura(inicio);
        int balanceamento = verBalanceamento(inicio);
        
        //se tiver muito pesada pra esquerda (pq ta positivo o 1), tem que rodar pra direita (se ele for menor que o pai)
        if (balanceamento > 1 && novo.nomePopular.compareTo(inicio.esquerda.nomePopular) < 0){
            inicio = rodarDireita(inicio);
        }
        //se tiver muito pesada pra esquerda (pq ta positivo o 1), tem que rodar pra esquerda e depois pra direita (se ele for maior que o pai, pq vai fazer um zigzag)
        if (balanceamento > 1 && novo.nomePopular.compareTo(inicio.esquerda.nomePopular) > 0){
            inicio.esquerda = rodarEsquerda(inicio.esquerda);
            inicio = rodarDireita(inicio);
        }
        //se tiver muito pesada pra direita (pq ta negativo o 1), tem que rodar pra esquerda (se ele for maior que o pai)
        if (balanceamento < -1 && novo.nomePopular.compareTo(inicio.direita.nomePopular) > 0){
            inicio = rodarEsquerda(inicio);
        }
        //se tiver muito pesada pra direita (pq ta negativo o 1), tem que rodar pra direita e depois pra esquerda (se ele for menor que o pai, pq vai fazer um zigzag)
        if (balanceamento < -1 && novo.nomePopular.compareTo(inicio.direita.nomePopular) < 0){
            inicio.direita = rodarDireita(inicio.direita);
            inicio = rodarEsquerda(inicio);
        }
        
        return inicio;
    }
    
    //funções para parte AVL
    //descobrir altura e balanceamento
    static int descobrirAltura(Tespecie animal){
        if (animal == null){
            return -1;
        } else {
            return animal.altura;
        }
    }
    
    static void definirAltura(Tespecie animal){
        animal.altura = 1 + Math.max(descobrirAltura(animal.esquerda), descobrirAltura(animal.direita));
    }
    
    static int verBalanceamento(Tespecie animal){
        return descobrirAltura(animal.esquerda) - descobrirAltura(animal.direita);
    }
    
    //rotacionar
    static Tespecie rodarDireita(Tespecie animal){
        Tespecie filhoEs = animal.esquerda;
        Tespecie aux = filhoEs.direita;
        
        // filhoEs vira pai e o elemento que era maior que filhoEs vira menor que o pai anterior (aux)
        filhoEs.direita = animal;
        animal.esquerda = aux;
        //atualiza as altura pra não ferrar com tudo
        definirAltura(animal);
        definirAltura(filhoEs);
        //retorna o filhoEs pq ele virou o paizão
        return filhoEs;
    }
    
    static Tespecie rodarEsquerda(Tespecie animal){
        Tespecie filhoDi = animal.direita;
        Tespecie aux = filhoDi.esquerda;
        
        // filhoDi vira pai e o elemento que era menor que filhoDi vira maior que o pai anterior (aux)
        filhoDi.esquerda = animal;
        animal.direita = aux;
        //atualiza as altura pra não ferrar com tudo
        definirAltura(animal);
        definirAltura(filhoDi);
        //retorna o filhoDi pq ele virou o paizão
        return filhoDi;
    }
    
    static void printarEspecie(Tespecie animal){
        System.out.println("\nNomenclatura: " + animal.nomenclatura + "\nNome Popular: " + animal.nomePopular + "\nBioma: " + animal.bioma + "\nStatus: " + animal.grauAmeaca);
    }
    
    static Tespecie buscarEspecie(String especie, Tespecie arvore){
        Tespecie w = arvore;
        
        if(w == null){
            return w;
        }
        
        if(w.nomePopular.equals(especie)){
            printarEspecie(w);
            return w;
        } else if(especie.compareTo(w.nomePopular) < 0){
            return buscarEspecie(especie, w.esquerda);
        } else if(especie.compareTo(w.nomePopular) > 0){
            return buscarEspecie(especie, w.direita);
        }
        
        return w;
    }
    
    
    static Tespecie removerEspecie(String especie, Tespecie arvore){
        if(arvore == null){
            System.out.println("\nA especie nao esta no catalogo ou foi digitada incorretamente.");
            return arvore;
        }
        
        if(especie.compareTo(arvore.nomePopular) < 0){
            arvore.esquerda = removerEspecie(especie, arvore.esquerda);
        }else if (especie.compareTo(arvore.nomePopular) > 0){
            arvore.direita = removerEspecie(especie, arvore.direita);
        //se ele for nó folha sem filhos
        }else if(descobrirAltura(arvore) == 0){
            return null;
        //se só tiver um filho (à direita)
        }else if(arvore.esquerda == null){
            return arvore.direita;
        //se só tiver um filho (à esquerda)
        }else if(arvore.direita == null){
            return arvore.esquerda;
        }else{
            Tespecie menorDir = arvore.direita;
            while(menorDir.esquerda != null){
                menorDir = menorDir.esquerda;
            }
            
            arvore.nomePopular = menorDir.nomePopular;
            arvore.nomenclatura = menorDir.nomenclatura;
            arvore.bioma = menorDir.bioma;
            
            arvore.direita = removerEspecie(arvore.nomePopular, arvore.direita);
        }
        
        definirAltura(arvore);
        int balanceamento = verBalanceamento(arvore);
        
        //aqui é possível fazer um if else diminuindo quantidade de código mas assim eu compreendo mais facilmente
        
        //mt pesado pra esquerda
        if(balanceamento > 1 && verBalanceamento(arvore.esquerda) >= 0){
            return rodarDireita(arvore);
        }
        //mt pesado pra direita
        if(balanceamento < -1 && verBalanceamento(arvore.direita) <= 0){
            return rodarEsquerda(arvore);
        }
        //zigzag da esquerda
        if(balanceamento > 1 && verBalanceamento(arvore.esquerda) < 0){
            arvore.esquerda = rodarEsquerda(arvore.esquerda);
            return rodarDireita(arvore);
        }
        //zigzag da direita
        if(balanceamento < -1 && verBalanceamento(arvore.direita) > 0){
            arvore.esquerda = rodarDireita(arvore.direita);
            return rodarEsquerda(arvore);
        }
        
        return arvore;
    }
    
    static void imprimirArvore(Tespecie raiz){
        if (raiz == null){
            return;
        }
        imprimirArvore(raiz.esquerda);
        printarEspecie(raiz);
        imprimirArvore(raiz.direita);
    }
    
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        
        //definindo esqueleto da árvore
        Tnodo raiz = new Tnodo();
        //binaria inicial
        Tnodo vertebrados = new Tnodo();
        Tnodo invertebrados = new Tnodo();
        raiz.primeiro = vertebrados;
        raiz.segundo = invertebrados;
        
        //categorias sub
        Tnodo terrestres = new Tnodo();
        Tnodo aquaticos = new Tnodo();
        Tnodo voadores = new Tnodo();
        Tnodo anfibios = new Tnodo();
        Tnodo terrestresIn = new Tnodo();
        Tnodo aquaticosIn = new Tnodo();
        Tnodo voadoresIn = new Tnodo();
        Tnodo anfibiosIn = new Tnodo();
        invertebrados.primeiro = terrestresIn;
        invertebrados.segundo = aquaticosIn;
        invertebrados.terceiro = voadoresIn;
        invertebrados.quarto = anfibiosIn;
        vertebrados.primeiro = terrestres;
        vertebrados.segundo = aquaticos;
        vertebrados.terceiro = voadores;
        vertebrados.quarto = anfibios;
        
        boolean superTrue = true;
        while(superTrue){
            System.out.println("\nMenu:\n1. Inserir especie no catalogo\n2. Remover especie do catalogo\n3. Buscar especie\n4. Imprimir sub-árvore\n0. Sair do programa");
            int opcao = teclado.nextInt();
            teclado.nextLine();
            
            switch(opcao){
                case 1:
                    Tnodo nodoCerto = categorizarEspecie(raiz, teclado);
                    nodoCerto.avlRaiz = inserirEspecie(nodoCerto.avlRaiz, criarEspecie(teclado));
                    break;
                case 2: 
                    nodoCerto = categorizarEspecie(raiz, teclado);
                    System.out.println("\nDigite o nome popular exato da especie que queira remover: ");
                    nodoCerto.avlRaiz = removerEspecie(teclado.nextLine(), nodoCerto.avlRaiz);
                    break;
                case 3: 
                    Tnodo grupoCategs[] = new Tnodo [8];
                    grupoCategs[0] = terrestres;
                    grupoCategs[1] = aquaticos;
                    grupoCategs[2] = voadores;
                    grupoCategs[3] = anfibios;
                    grupoCategs[4] = terrestresIn;
                    grupoCategs[5] = aquaticosIn;
                    grupoCategs[6] = voadoresIn;
                    grupoCategs[7] = anfibiosIn;
                    System.out.println("\nDigite o nome popular exato da especie que queira pesquisar: ");
                    String especieBusca = teclado.nextLine();
                    
                    //pra não precisar de uma cadeia de ifs gigante
                    for(int i=0; i<8; i++){
                        buscarEspecie(especieBusca, grupoCategs[i].avlRaiz);
                    }
                    break;
                case 4:
                    nodoCerto = categorizarEspecie(raiz, teclado);
                    imprimirArvore(nodoCerto.avlRaiz);
                    break;
                case 0: superTrue = false;
                    break;
                default: 
                    System.out.println("\nOpcao invalida. Por favor tente novamente.");
                    break;
            }
        }
    }
}

// coisas a adicionar:
//melhor verificação de duplicata
//identificação da família ao pesquisar

