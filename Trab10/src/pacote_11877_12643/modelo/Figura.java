package pacote_11877_12643.modelo;

import java.awt.Color;
import java.awt.Graphics;
import java.util.*;

/**
 * Representa uma figura como um conjunto de formas desenháveis.
 */
public class Figura implements FormaDesenhavel
{
	/**
	 * Conjunto de segmentos.
	 */
	protected List<FormaPlotavel> listaFormasPlotaveis;

	// Construtor
	public Figura()
	{
		listaFormasPlotaveis = new ArrayList<FormaPlotavel>();
	}

	// Metodos
	/**
	 * Adiciona um novo segmento ao conjunto.
	 */
	public boolean add(FormaPlotavel forma)
	{
		return listaFormasPlotaveis.add(forma);
	}

	/**
	 * @return O conjunto de segmentos.
	 */
	public List<FormaPlotavel> getFormas()
	{
		return listaFormasPlotaveis;
	}

	public void desenhar(Graphics desenho, Color cor)
	{
		for (FormaDesenhavel forma : listaFormasPlotaveis)
			forma.desenhar(desenho, cor);
	}

	/**
	 * Aplica uma transformacao a figura atual.
	 * 
	 * @param transformacao
	 * @return uma nova figura com a transformacao aplicada
	 */
	public Figura fazTransformacao(Transformacao transformacao)
	{
		// Declaracao de variaveis.
		FormaPlotavel novaForma;
		List<Ponto> novosPontos;
		Ponto novoP;

		// Varredura da lista de formas plotaveis e aplicacao da transformacao
		// ponto a ponto.
		Figura novaFigura = new Figura();
		for (FormaPlotavel forma : listaFormasPlotaveis)
		{
			novaForma = new FormaPlotavel();
			novaFigura.add(novaForma);

			novosPontos = novaForma.getListaPontos();

			for (Ponto p : forma.getListaPontos())
			{
				novoP = transformacao.calcula(p.getX(), p.getY());
				novosPontos.add(novoP);
			}
		}

		return novaFigura;
	}

	/**
	 * Aplica uma transformacao que exige ponto de referencia
	 * 
	 * @param transformacao
	 * @param pontoReferencia
	 * @return uma nova figura com a transformacao aplicada
	 */
	public Figura fazTransformacao(Transformacao transformacao,
			Ponto pontoReferencia)
	{
		// Declaracao de variaveis.
		Figura f;
		Transformacao t;

		// Translacao a origem.
		t = new Transformacao();
		t.fazTranslacao(-pontoReferencia.getX(), -pontoReferencia.getY());
		f = fazTransformacao(t);

		// Operacao
		f = f.fazTransformacao(transformacao);

		// Translacao de volta.
		t = new Transformacao();
		t.fazTranslacao(pontoReferencia.getX(), pontoReferencia.getY());
		f = f.fazTransformacao(t);

		return f;
	}

	/**
	 * Valida uma figura quanto a um viewport
	 * 
	 * @param figura
	 * @param altura
	 * @param largura
	 * @return a figura validade
	 */
	public static Figura valida(Figura figura, int altura, int largura)
	{
		// Varre a lista de formas da figura e valida uma a uma.
		for (FormaPlotavel forma : figura.listaFormasPlotaveis)
		{
			if (forma.validar(altura, largura) == false)
				return null;
		}

		return figura;
	}

	public Figura corteRetas(Viewport janela)
	{
		Figura novaFigura;
		Segmento seg;
		
		novaFigura = new Figura();
		
		// Varre a lista de formas da figura e valida uma a uma.
		for (FormaPlotavel forma : listaFormasPlotaveis)
		{
			if (forma instanceof Segmento == true)
			{
				seg = ((Segmento) forma).corte(janela);
				if (seg != null)
					novaFigura.add(seg);
			}
		}
		
		return novaFigura;
	}

	public Figura cortePoligonos(Viewport janela)
	{
		List<Ponto> listaVerticesIniciais, listaVerticesFinais;
		Figura novaFigura;
		Segmento seg;
		
		listaVerticesIniciais = new ArrayList<Ponto>();
		novaFigura = new Figura();
		
		listaVerticesIniciais.add(((Segmento) listaFormasPlotaveis.get(0)).getPInicial());
		// Varre a lista de formas da figura e valida uma a uma.
		for (FormaPlotavel forma : listaFormasPlotaveis)
		{
			if (forma instanceof Segmento == true)
			{
				seg = (Segmento) forma;
				listaVerticesIniciais.add(seg.getPFinal());
			}
		}
		
		/*listaVerticesIniciais.add(new Ponto(100, 100));
		listaVerticesIniciais.add(new Ponto(150, 100));
		listaVerticesIniciais.add(new Ponto(200, 200));
		listaVerticesIniciais.add(new Ponto(230, 250));
		listaVerticesIniciais.add(new Ponto(150, 300));
		listaVerticesIniciais.add(new Ponto(120, 200));
		listaVerticesIniciais.add(new Ponto(100, 100));*/

		
		listaVerticesFinais = listaVerticesIniciais;
		
		for (Direcoes direcao: Direcoes.values())
			listaVerticesFinais = janela.cortePoligonos(listaVerticesFinais, direcao);

		Ponto p2, p1 = listaVerticesFinais.get(0);
		for (int i=1; i<listaVerticesFinais.size(); i++)
		{
			p2 = listaVerticesFinais.get(i);
			novaFigura.add(new Segmento(p1, p2));
			p1 = p2;
		}
		
		return novaFigura;
	}

}
