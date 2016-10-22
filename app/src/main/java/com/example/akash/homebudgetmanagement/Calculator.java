package com.example.akash.homebudgetmanagement;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Stack;
import java.util.Vector;

public class Calculator extends Activity implements View.OnClickListener {

    private TextView Display;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Display=(TextView)findViewById(R.id.textDisplay);

        int idList[] = {R.id.btn0, R.id.btn1,R.id.btn2,R.id.btn3,R.id.btn4,R.id.btn5,R.id.btn6,R.id.btn7,R.id.btn8,R.id.btn9,
                R.id.btnAdd ,R.id.btnSub,R.id.btnMult,R.id.btnDiv,R.id.btnClear,R.id.btnResult,R.id.btnBack,R.id.btnPoint,
                R.id.btnLeftBracket,R.id.btnRightBracket,R.id.btnExponents,R.id.btnPercent,R.id.btnOk,R.id.btnCancel};

        for(int id : idList){
            View v = findViewById(id);
            v.setOnClickListener(this);
        }
    }

    public void addToScreen(String str){
        String DispCurrent = Display.getText().toString();
        DispCurrent += str;
        Display.setText(DispCurrent);
        //Display.setText(MainActivity.getDoubleText(Double.parseDouble(DispCurrent)));
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.btnClear :
                Display.setText("");
                break;
            case R.id.btnBack :
                String curDisp =Display.getText().toString();
                if(!curDisp.isEmpty()){
                    curDisp=curDisp.substring(0,curDisp.length()-1);
                    Display.setText(curDisp);
                }
                break;
            case R.id.btnResult :
                generateResult();
                break;
            case R.id.btnCancel :
                CancelListener();
                break;
            case R.id.btnOk :
                OkListener();
                break;
            default:
                String str = ((Button)v).getText().toString();
                addToScreen(str);
                break;
        }
    }

    public class Content {

        public double number;
        public char symbol;
        public boolean flag=true;

        public Content(Double number){
            this.number=number;
            flag=true;
        }

        public Content(Character symbol){
            this.symbol=symbol;
            flag=false;
        }
    }

    Vector<Content> postfix = new Vector<>();

    public Vector<Content> getInfix(String inputString){

        //here initial is used for if there contains a +/- operator at the first of expression
        double initial=1.0;
        Vector<Content> infix =new Vector<>();
        inputString=inputString.replaceAll(" ", "");
        if(inputString.charAt(0)=='-'){
            initial=-1.0;
        }
        if(inputString.charAt(0)=='+'||inputString.charAt(0)=='-')
            inputString=inputString.replaceFirst("[+-]", "");

        String numbers=inputString;
        String temp=numbers.replaceAll("[+%^*/-]"," ").replaceAll("\\)"	,"").replaceAll("\\(", "");
        numbers=numbers.replaceAll("[+%^*/-]"," ").replaceAll("\\)"	," ").replaceAll("\\(", " ");

        if(temp.charAt(0) == ' ') temp.replaceFirst(" ", "");

        String[] tokens = temp.split("[ ]");

        Vector <Double> numbersObtained=new Vector<>();

        for(int i=0;i<tokens.length;i++){
            if(i==0)numbersObtained.add((initial*Double.parseDouble(tokens[i])));//multiply first number by initial

            else numbersObtained.add(Double.parseDouble(tokens[i]));
        }

        for(int i=0;i<numbers.length();i++){
            if(numbersObtained.isEmpty())
                break;

            if(numbers.charAt(i)==' '){
                if(i!=0){
                    if(numbers.charAt(i-1)!=' '){
                        infix.add(new Content(numbersObtained.elementAt(0)));
                        numbersObtained.remove(0);
                        infix.add(new Content(inputString.charAt(i)));
                    }
                    else
                        infix.add(new Content(inputString.charAt(i)));
                }
                else
                    infix.add(new Content(inputString.charAt(i)));

            }
        }
        if(!numbersObtained.isEmpty()){
            infix.add(new Content(numbersObtained.elementAt(0)));
            numbersObtained.remove(0);
        }

        return infix;
    }

    private Boolean lowerPrecedence(char infixSymbol , char stackSymbol){
        if((infixSymbol=='*'||infixSymbol=='/' || infixSymbol == '^'||infixSymbol == '%')&&(stackSymbol=='+'||stackSymbol=='-')) return true;
        else return false;
    }

    public void convertToPostfix(String inputString){

        postfix.clear();
        Vector<Content> infix = getInfix(inputString);
        Stack<Content> myStack=new Stack<>();

        myStack.push(new Content('('));
        infix.add(new Content(')'));

        for(int i=0; i<infix.size(); i++){

            if(infix.elementAt(i).flag)
                postfix.add(infix.elementAt(i));
            else{
                if(infix.elementAt(i).symbol == '(')
                    myStack.push(infix.elementAt(i));
                else if(infix.elementAt(i).symbol == ')'){
                    while(true){
                        Content tempNode;
                        tempNode=myStack.lastElement();
                        if(tempNode.symbol == '('){
                            myStack.pop();
                            break;
                        }
                        else{
                            postfix.add(tempNode);
                            myStack.pop();
                        }
                    }
                }
                else{
                    while(true){
                        Content tempNode1 ;
                        tempNode1=myStack.lastElement();
                        if(lowerPrecedence(infix.elementAt(i).symbol, tempNode1.symbol)|| tempNode1.symbol == '(') break;
                        postfix.add(tempNode1);
                        myStack.pop();

                    }
                    myStack.push(infix.elementAt(i));
                }
            }
        }
    }

    public double evaluatePostfixExpression(){

        Stack<Double> myStack=new Stack<>();

        for(int i=0;i<postfix.size();i++){
            if(postfix.elementAt(i).flag)
                myStack.push(postfix.elementAt(i).number);
            else{
                double stackTop0, stackTop1, result;
                stackTop0 = myStack.peek();
                myStack.pop();
                stackTop1 = myStack.peek();
                myStack.pop();

                result = calculate(stackTop0, stackTop1, postfix.elementAt(i).symbol);
                myStack.push(result);

            }
        }

        return myStack.peek();
    }

    public double  calculate(double a, double b, char symbol){
        switch(symbol){
            case '+': return (a+b);
            case '-': return (b-a);
            case '*': return (a*b);
            case '^': return Math.pow(b, a);
            case '%': return (b%a);
            default: return (b/a);
        }
    }

    public void generateResult()
    {
        String result;
        try {
            convertToPostfix(Display.getText().toString());
            result=MainActivity.getDoubleText(evaluatePostfixExpression());
            Display.setText(result);
        }catch (Exception e){
            Display.setText("Math Error");
        }
    }

    public void OkListener(){

        Intent data = new Intent();
        data.putExtra("number",Display.getText().toString());
        setResult(Activity.RESULT_OK,data);
        finish();
    }

    public void CancelListener(){
        finish();
    }
}
