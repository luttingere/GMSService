package com.luttinger.gcm;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.android.gcm.server.Message;
import com.google.android.gcm.server.MulticastResult;
import com.google.android.gcm.server.Sender;



public class GCMBroadcast extends HttpServlet {

	private static final long serialVersionUID = 1L;

    /**
     * Clave que se genero cuando se creo el API KEY en el google api project
     */
    //private static final String SENDER_ID = "126945037882";
    //private static final String SENDER_ID = "AIzaSyAT0VKPF-vvZwNygZB2vBkwBkORsA_BKfc";
    private static final String SENDER_ID = "AIzaSyAZKetf3ifsDpOXYUYg9J4HG5tsHozJkAk";


    /**
     * Codigo de registro del dispositivo android utilizando el mismo SENDER-ID
     */
    private static final String ANDROID_DEVICE = "APA91bFxoyv37_eALEOXGss2m9MshHOsNUTDLUkQHrtvB1CDtCmCe4_HqT6u4NmO-5miYrlgT_e3pPeJJ9iM9t_90GCD3jo8apClQKde7D6-t-G5NfSFGmJdSN4d_tPAOsM9Su-SPDTX4DMGhEweIz8mqMO2xmPL6g";

		
	//Arreglo para almacenar todos los ANDROID_DEVICE a los cuales se les enviara el mensaje, para esta prueba solo usaremos uno
	private List<String> androidTargets = new ArrayList<String>();
       

    public GCMBroadcast() {
    	
        super();

        androidTargets.add(ANDROID_DEVICE);
        
    }


    /**
     * Metodo ejecutado desde el archivo index.jsp para enviar el mensaje
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {



        /**
         *
         */
        String clave = "";
        /**
         *
         */
        String mensajeAEnviar = "";
		
		try {
			mensajeAEnviar = request.getParameter("Message");
			clave = request.getParameter("CollapseKey");
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}


		Sender sender = new Sender(SENDER_ID);


        Message.Builder messageBuilder = new Message.Builder();

        messageBuilder.collapseKey(clave);
        messageBuilder.timeToLive(30);
        messageBuilder.delayWhileIdle(true);
        messageBuilder.addData("message", mensajeAEnviar);
		Message message = messageBuilder.build();


		try {


			MulticastResult result = sender.send(message, androidTargets, 1);
			
			if (result.getResults() != null) {
				int canonicalRegId = result.getCanonicalIds();
				if (canonicalRegId != 0) {

                    System.out.println(" Envio el mensaje  ");
                }
			} else {

				int error = result.getFailure();
				System.out.println("Broadcast failure: " + error);

			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}


		request.setAttribute("CollapseKey", clave);
		request.setAttribute("Message", mensajeAEnviar);
		request.getRequestDispatcher("index.jsp").forward(request, response);
				
	}

}
